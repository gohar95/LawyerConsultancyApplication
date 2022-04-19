package paklegal2020.pl.paklegal

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import paklegal2020.pl.paklegal.Models.UserModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Signup : AppCompatActivity() {
    private lateinit var user_name: EditText
    private lateinit var user_email: EditText
    private lateinit var user_password: EditText
    private lateinit var uc_password: EditText
    private lateinit var user_phone: EditText
    private lateinit var user_address: EditText
    private lateinit var signup_btn: Button
    private lateinit var image_btn: Button
    private lateinit var btnLocation: Button
    private lateinit var s1: TextView
    private lateinit var s2: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var SOURCE_PLACE = 1000
    var placeFields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
    )
    private lateinit var moveAccount: TextView
    private lateinit var showpath: TextView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var select_log: Switch
    private lateinit var auth: FirebaseAuth
    private lateinit var selectedImage: Uri
    var status_pick = false
    private lateinit var DownloadUrl: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var reference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_signup)
        Places.initialize(this, getString(R.string.places_api))
        progressDialog = ProgressDialog(this)
        storage = FirebaseStorage.getInstance()
        reference = storage!!.reference
        auth = FirebaseAuth.getInstance()
        s1 = findViewById(R.id.textView)
        s2 = findViewById(R.id.textView2)

        sharedPreferences = getSharedPreferences("MapsLocation", Context.MODE_PRIVATE);
        user_name = findViewById(R.id.user_name)
        user_email = findViewById(R.id.user_email)
        user_password = findViewById(R.id.user_password)
        uc_password = findViewById(R.id.uc_password)
        user_phone = findViewById(R.id.user_phone)
        signup_btn = findViewById(R.id.signup_btn)
        moveAccount = findViewById(R.id.moveAccount)
        select_log = findViewById(R.id.select_log)
        user_address = findViewById(R.id.user_address)
        image_btn = findViewById(R.id.choose_btn)
        showpath = findViewById(R.id.show_path)
        btnLocation = findViewById(R.id.btn_location)
        btnLocation.setOnClickListener {
            val intent: Intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, placeFields
            )
                    .build(this)
            startActivityForResult(intent, SOURCE_PLACE)
        }
        image_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        })
        select_log.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b -> if (b) select_log.setText("Client") else select_log.setText("Lawyer") })
        s2.setTypeface(s2.getTypeface(), Typeface.BOLD)
        s1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@Signup, LoginActivity::class.java))
            finish()
        })
        moveAccount.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@Signup, LoginActivity::class.java))
            finish()
        })
        signup_btn.setOnClickListener(View.OnClickListener { CheckValidation() })
    }

    private fun CheckValidation() {
        val Name = user_name!!.text.toString()
        val Email = user_email!!.text.toString()
        val Password = user_password!!.text.toString()
        val CPassword = uc_password!!.text.toString()
        val Phone = user_phone!!.text.toString()
        val Address = user_address!!.text.toString()
        if (TextUtils.isEmpty(Name)) {
            user_name!!.error = "Enter Name Here"
        } else if (TextUtils.isEmpty(Email) || !Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            user_email!!.error = "Enter Email here"
        } else if (TextUtils.isEmpty(Password)) {
            user_password!!.error = "Enter Password here"
        } else if (TextUtils.isEmpty(CPassword)) {
            uc_password!!.error = "Again Enter here"
        } else if (TextUtils.isEmpty(Phone)) {
            user_phone!!.error = "Enter Phone here"
        } else if (TextUtils.isEmpty(Address)) {
            user_address!!.error = "Enter Address here"
        } else if (Password == CPassword) {
            if (select_log!!.text.toString() == "Lawyer") {
                UpdateLawyerData(Name, Email, Password, Phone, Address, "Lawyer")
            }
            if (select_log!!.text.toString() == "Client") {
                UpdateClientData(Name, Email, Password, Phone, Address, "Client")
            }
        } else {
            Toast.makeText(this, "Password Not Matched", Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateClientData(name: String, email: String, password: String, phone: String, address: String, status: String) {
        progressDialog!!.setMessage("Data Uploaded\nPlease Wait...")
        progressDialog!!.show()
        progressDialog!!.setCancelable(false)
        auth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val ref = reference!!.child("ClientImages/" + auth!!.currentUser!!.uid)
                ref.putFile(selectedImage!!).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    ref.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        status_pick = false
                        DownloadUrl = task.result!!
                        UploadDatainClient(name, email, password, phone, address, "Client", DownloadUrl)
                        progressDialog!!.dismiss()
                        Toast.makeText(this@Signup, "URL Created", Toast.LENGTH_SHORT).show()
                    } else {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@Signup, "URL Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                Toast.makeText(this@Signup, "Authentication Working", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Signup, "Authentication Failed", Toast.LENGTH_SHORT).show()
                progressDialog!!.dismiss()
            }
        }
    }

    private fun UploadDatainClient(name: String, email: String, password: String, phone: String, address: String, status: String, downloadUrl: Uri?) {
        @SuppressLint("SimpleDateFormat") val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date = Date()
        val DateTime = dateFormat.format(date)
        val token = FirebaseInstanceId.getInstance().token

        val values = UserModel(token, name, email, password, phone, address, status, downloadUrl.toString(), DateTime)
        FirebaseDatabase.getInstance().getReference("Client").child(auth!!.currentUser!!.uid).setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog!!.dismiss()
                        val i = Intent(this@Signup, LoginActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        progressDialog!!.dismiss()
                        //Alert Dialog
                        val alertDialog = AlertDialog.Builder(applicationContext).create()
                        alertDialog.setTitle("Uploading Failed")
                        alertDialog.setCancelable(false)
                        alertDialog.setMessage("Due to\nTechnical Issue\nTry Again")
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                        ) { dialog, which -> dialog.dismiss() }
                        //alertDialog.show();
                    }
                }
    }

    private fun UpdateLawyerData(name: String, email: String, password: String, phone: String, address: String, lawyer: String) {
        progressDialog!!.setMessage("Data Uploaded\nPlease Wait...")
        progressDialog!!.show()
        progressDialog!!.setCancelable(false)

        auth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val ref = reference!!.child("LawyerImages/" + auth!!.currentUser!!.uid)
                ref.putFile(selectedImage!!).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw Objects.requireNonNull(task.exception)!!
                    }
                    ref.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        status_pick = false
                        DownloadUrl = task.result!!
                        val loc = sharedPreferences.getString("location", "")
                        UploadDatainLawyer(name, email, password, phone, address, "Lawyer", DownloadUrl)
                        progressDialog!!.dismiss()
                        Toast.makeText(this@Signup, "URL Created", Toast.LENGTH_SHORT).show()
                    } else {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@Signup, "URL Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                Toast.makeText(this@Signup, "Authentication Working", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Signup, "Authentication Failed", Toast.LENGTH_SHORT).show()
                progressDialog!!.dismiss()
            }
        }
    }

    private fun UploadDatainLawyer(name: String, email: String, password: String, phone: String, address: String, status: String, downloadUrl: Uri?) {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date = Date()
        val DateTime = dateFormat.format(date)
        val token = FirebaseInstanceId.getInstance().token

        val values = UserModel(token, name, email, password, phone, address, status, downloadUrl.toString(), DateTime)
        FirebaseDatabase.getInstance().getReference("Lawyer").child(Objects.requireNonNull(auth!!.currentUser)!!.uid).setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog!!.dismiss()
                        val i = Intent(this@Signup, LoginActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        progressDialog!!.dismiss()
                        //Alert Dialog
                        val alertDialog = AlertDialog.Builder(applicationContext).create()
                        alertDialog.setTitle("Uploading Failed")
                        alertDialog.setCancelable(false)
                        alertDialog.setMessage("Due to\nTechnical Issue\nTry Again")
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
                        ) { dialog, which -> dialog.dismiss() }
                        //alertDialog.show();
                    }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedImage = data!!.data!!
            status_pick = true
            showpath!!.text = selectedImage.toString()
        } else if (requestCode == SOURCE_PLACE && resultCode == Activity.RESULT_OK) {
            val sourceplace = Autocomplete.getPlaceFromIntent(data!!)
            val queriedLocation = sourceplace.latLng
            val sourcename   = sourceplace.name
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString("location", sourcename)

            editor.apply()
           Toast.makeText(this@Signup, queriedLocation!!.latitude.toString() + "," + queriedLocation!!.longitude.toString(), Toast.LENGTH_LONG).show()
            Toast.makeText(this@Signup, sourcename.toString(), Toast.LENGTH_LONG).show()

        }
    }
}