package com.sbiitju.smartremainder.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sbiitju.smartremainder.ClassSave.ExamSchedule;
import com.sbiitju.smartremainder.ClassSave.FirstClass;
import com.sbiitju.smartremainder.ClassSave.MainClass;
import com.sbiitju.smartremainder.ClassSave.UserInfo;
import com.sbiitju.smartremainder.ClassSave.Value;
import com.sbiitju.smartremainder.R;
import com.sbiitju.smartremainder.SqliteDatabase.CourseDatabase;
import com.sbiitju.smartremainder.SqliteDatabase.MydatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String getcode,getexamcode;
    FirebaseAuth firebaseAuth;
    MydatabaseHelper mydatabaseHelper;
    CourseDatabase courseDatabase;
    FloatingActionButton floatingActionButton;
    ArrayList<String> arrayList,arrayList1;
    TextView userid;
    AutoCompleteTextView autoCompleteTextView;
    Button setExam,getExam;
    DatePicker datePicker;
    TimePicker timePicker,settimepicker;
    int day,month,year,examhour,exammin;
    String firstclass;
    String firstclasstime;

//floating activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        mydatabaseHelper=new MydatabaseHelper(this);
        courseDatabase=new CourseDatabase(this);
        autoCompleteTextView=findViewById(R.id.searchview);
        setExam=findViewById(R.id.examset);
        getExam=findViewById(R.id.examget);
        setExam.setText("Set"+"\n"+"Exam"+"\n"+"Schedule");
        getExam.setText("Get"+"\n"+"Exam"+"\n"+"Schedule");
        SQLiteDatabase sqLiteDatabase=mydatabaseHelper.getWritableDatabase();
    firebaseAuth= FirebaseAuth.getInstance();
    floatingActionButton=findViewById(R.id.fab);
    userid=findViewById(R.id.userid);
        final DatabaseReference data= FirebaseDatabase.getInstance().getReference(firebaseAuth.getCurrentUser().getUid());
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserInfo userInfo=dataSnapshot.getValue(UserInfo.class);
                    String name=userInfo.getName();
                    String institute=userInfo.getInstitution();
                    userid.setText(name+"\n"+firebaseAuth.getCurrentUser().getEmail()+"\n"+institute);
                }
                else {
                    final View view= getLayoutInflater().inflate(R.layout.userinfo,null);
                    final android.app.AlertDialog.Builder builder5=new android.app.AlertDialog.Builder(MainActivity.this);
                    builder5.setView(view);
                    final EditText name,phone,institution;
                    name=view.findViewById(R.id.name);
                    phone=view.findViewById(R.id.number);
                    institution=view.findViewById(R.id.institution);
                    Button goahead;
                    goahead=view.findViewById(R.id.goahead);
                    goahead.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String n=name.getText().toString();
                            String p=phone.getText().toString();
                            String in=institution.getText().toString();
                            UserInfo userInfo=new UserInfo(n,p,in);
                            data.setValue(userInfo);
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                    builder5.setView(view).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View view=getLayoutInflater().inflate(R.layout.insertshow,null);
            final Button insert,show,update;
            final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            insert=view.findViewById(R.id.insert);
            show=view.findViewById(R.id.show);
            update=view.findViewById(R.id.update);
            insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
                    final AutoCompleteTextView first;
                    final TimePicker t1;
                    final Spinner spinner,class_day;
                    Button savebut;
                    View view1=getLayoutInflater().inflate(R.layout.classroutine,null);
                    spinner=view1.findViewById(R.id.spinner);
                    class_day=view1.findViewById(R.id.classcourse);
                    t1=view1.findViewById(R.id.firstclasstime);
                    savebut=view1.findViewById(R.id.firstclassBut);
                    first=view1.findViewById(R.id.firstclasscoursenamesave);
//                    ArrayAdapter<String> adapter=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,arrayList);
//                    first.setAdapter(adapter);
//                    first.setThreshold(1);
                    savebut.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            String spinvalue=spinner.getSelectedItem().toString();
                            String classday=class_day.getSelectedItem().toString();
                            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                            dialog.setMessage("Saving..");
                            dialog.show();
                            firstclass= first.getText().toString();
                            firstclasstime=String.valueOf(t1.getHour()).concat(":").concat(String.valueOf(t1.getMinute()));
                            if(firstclass.isEmpty()){
                                first.setError("Input Here!!");
                                dialog.hide();
                            }
                            else{
                               FirstClass firstClass=new FirstClass(firstclass,firstclasstime);
                                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(classday.substring(0,1).concat(
                                        spinvalue.substring(0,2).concat(firebaseAuth.getCurrentUser().getEmail().substring(0,10))));
                                databaseReference.setValue(firstClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                            dialog.hide();
                                        }
                                    }
                                });
                            }

                        }
                    });
                    builder1.setView(view1).show();
                }
            });
            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Displying..");
                    dialog.show();
                    StringBuffer stringBuffer=show();
                    if(stringBuffer==null){
                      dialog.hide();
                        Toast.makeText(MainActivity.this, "Database is Empty", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        showresult("Saved Course!!",stringBuffer.toString());
                    dialog.hide();
                    }
                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    View mview=getLayoutInflater().inflate(R.layout.dialog,null);
                    final EditText email=mview.findViewById(R.id.email);
                    final EditText id=mview.findViewById(R.id.id);
                    Button login=mview.findViewById(R.id.login);
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                            dialog.setMessage("Updating..");
                            dialog.show();
                            if(email.getText().toString().isEmpty()||id.getText().toString().isEmpty()){
                                email.setError("Please fil up!");
                                id.setText("Please input ur id:");
                            }else{
                                int uid=Integer.valueOf(id.getText().toString());
                                String uname=email.getText().toString();
                                boolean u=mydatabaseHelper.update(uid,uname);
                                if(u==true){
                                    Toast.makeText(MainActivity.this, uid+ " number id is Updated", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                                }
                            }
                            email.setText("");
                            id.setText("");
                        }
                    });
                    builder.setView(mview).show();


                }
            });
            builder.setView(view).show();

        }

    });

    ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.day));
    autoCompleteTextView.setAdapter(adapter);
    autoCompleteTextView.setThreshold(1);
    final Spinner spinner;
    spinner=findViewById(R.id.classss);
   autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//           AKhan theke
           final String autocomplete=autoCompleteTextView.getText().toString();
           //FirstClass firstClass=new FirstClass(firstclass,firstclasstime);
           final String mainclass_select=spinner.getSelectedItem().toString();
           String key=mainclass_select.substring(0,1).concat(
                   autocomplete.substring(0,2).concat(firebaseAuth.getCurrentUser().getEmail().substring(0,10)));
           DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(key);

           //Toast.makeText(MainActivity.this, key, Toast.LENGTH_SHORT).show();
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if(dataSnapshot.exists()){
                       AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                       FirstClass firstClass1=dataSnapshot.getValue(FirstClass.class);
                       Toast.makeText(MainActivity.this, firstClass1.getCourse(), Toast.LENGTH_SHORT).show();
                       builder.setTitle("Class Schedule!!");
                       builder.setMessage("Class Course: "+firstClass1.getCourse()+"\n"+"Class Time: "+firstClass1.getTime()+"\n"+"Thank you for using this App").show();
                   }
                   else{
                       Toast.makeText(MainActivity.this, "Hey Dear!!!"+"\n"+"Smile pls.."+"\n"+"No Class Found..", Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
                   Toast.makeText(MainActivity.this, "Faield!!", Toast.LENGTH_SHORT).show();

               }
           });


       }
   });
    }
    //Insert Method

    private long insertmethod(String id) {
        long rowid= mydatabaseHelper.insert(id);
        if(rowid>0){
        }
        else{
            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        }
        return rowid;
    }
//Menuuuuuuuuuu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
//MenuListener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout) {
            firebaseAuth.signOut();
            Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        if(id==R.id.about){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Developer");
            builder.setMessage(" Shahin Bashar"+"\n"+"         &"+
                    "\n"+"Bhabna Mukherjee"+"\n"+"....................................."+
                    "\n"+"          IIT"+"\n"+"Jahangirnagar University"+
                    "\n"+"shahinbashar2@gmail.com"+"\n"
                    +"bhabnamukherjee98@gmail.com"+"\n"+"#shahinbashr2")
                    .setIcon(R.drawable.shahin)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }
    public void showresult(String title,String data){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(data);
        builder.setCancelable(true);
        builder.show();

    }
    public StringBuffer show(){
        Cursor cursor=mydatabaseHelper.show();
        if(cursor.getCount()==0){
            return null;
        }
        StringBuffer stringBuffer=new StringBuffer();
        while(cursor.moveToNext()){
            stringBuffer.append("ID: "+cursor.getString(0)+"\n");
            stringBuffer.append("UserCode: "+cursor.getString(1)+"\n");
        }
        return stringBuffer;
    }
//Starttttttttttttttttttttttttttttttttt
    @Override
    protected void onStart() {
        Cursor getCursor=mydatabaseHelper.show();
        if(getCursor.getCount()==0){
            super.onStart();
        }
        arrayList = new ArrayList<>();
        while(getCursor.moveToNext()){
            arrayList.add(getCursor.getString(1));
        }
        Cursor setcourser=courseDatabase.show();
        if(setcourser.getCount()==0){
            super.onStart();
        }
        arrayList1 = new ArrayList<>();
        while(setcourser.moveToNext()){
            arrayList1.add(setcourser.getString(1));
        }
        super.onStart();

    }
//SetttttttttttttttttttButtttttttttttttooooooooooooonnnnnnnnnnnn
    public void SetButton(View view) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater= LayoutInflater.from(MainActivity.this);
        final View view1=inflater.inflate(R.layout.add1,null);
        final EditText description;
        //final Spinner h,m,am;
        final AutoCompleteTextView code;
        Button button;
        button=view1.findViewById(R.id.setbutoon);
//        h=view1.findViewById(R.id.hour);
//        am=view1.findViewById(R.id.amp);
//        m=view1.findViewById(R.id.min);
        settimepicker=view1.findViewById(R.id.timeset);
        description=view1.findViewById(R.id.description);
        code=view1.findViewById(R.id.id);
        ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList1);
        code.setThreshold(1);
        code.setAdapter(stringArrayAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String c=code.getText().toString();
                Cursor i=courseDatabase.search(c);
                if(i.getCount()==0){
                    courseDatabase.insert(c);
                }
                if(c.isEmpty()){
                    code.setError("Enter Course Code");
                }
                else{
                    String code=firebaseAuth.getCurrentUser().getEmail().substring(0,10);
                    String d=c.concat(code);
                    final String hour_string=String.valueOf(settimepicker.getHour());
                    final String des=description.getText().toString();
//                if(hour.isEmpty()){
//                    editText.setError("Enter Hour");
//                }
                    final String min=String.valueOf(settimepicker.getMinute());
                    int hour_int=Integer.valueOf(hour_string);
                    final String hour=String.valueOf(hour_int);
                    final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Alarm is setting...");
                    dialog.show();
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(d);
                    Value value=new Value(c,hour,min,des);
                    databaseReference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);

                                    builder1.setMessage("Hey Smart User!!"+"\n"+"Alarm set at "+hour+":"+min+" AM"+"\n"+"Thank You!!").show();
                                //Toast.makeText(MainActivity.this, "Alarm set at "+hour+"."+min, Toast.LENGTH_SHORT).show();
                                if(des.isEmpty()){
                                    Toast.makeText(MainActivity.this, "Description is null", Toast.LENGTH_SHORT).show();
                                }
                                dialog.hide();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                            }
                        }
                    });

                }

            }
        });
        builder.setView(view1).setIcon(R.drawable.shahin).show();
    }

    //GetttttttttttttttttttButtttttttttttooooooooooooonnnnnnnnnnnnnnnnn

    public void getButton(View view) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater= LayoutInflater.from(MainActivity.this);
        final View view2=inflater.inflate(R.layout.get,null);
        final AutoCompleteTextView e;
        Button b;
        e=view2.findViewById(R.id.getcode);
        b=view2.findViewById(R.id.getbut);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        e.setThreshold(1);
        e.setAdapter(adapter);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcode=e.getText().toString();
                if(getcode.isEmpty()){
                    e.setError("Enter a valid USERID");
                }else {

                    Cursor d=mydatabaseHelper.search(getcode);
                    if(d.getCount()==0){
                        insertmethod(getcode);
                    }
                    final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Finding an Alarm....");
                    dialog.show();
                    final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(getcode);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final Value value=dataSnapshot.getValue(Value.class);
                                if(dataSnapshot.exists()){
                                    AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
                                    builder1.setCancelable(false);
                                    builder1.setMessage("Course Code: "+value.getCode()+"\n"+"Class Time: "+value.getHour()+":"+value.getMin()+"\n"+"Messege: "+value.getDescription()+"\n"+"Do you want to SET Alarm??");
                                    builder1.setTitle("Class Schedule!!");
                                    builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog1, int which) {
                                            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                                                    .putExtra(AlarmClock.EXTRA_MESSAGE, getcode+"\n"+value.getDescription())
                                                    .putExtra(AlarmClock.EXTRA_HOUR, Integer.valueOf(value.getHour()))
                                                    .putExtra(AlarmClock.EXTRA_MINUTES, Integer.valueOf(value.getMin()));
                                            if (intent.resolveActivity(getPackageManager()) != null) {
                                                startActivity(intent);
                                                dialog.hide();
                                            }
                                        }
                                    });
                                    builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog2, int which) {
                                            dialog2.cancel();
                                            dialog.hide();
                                        }
                                    });
                                    builder1.show();
                                    Toast.makeText(MainActivity.this, "Yess!! got it!!", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                    Toast.makeText(MainActivity.this, "No Alarm Found"
                                            +"\n"+"Input a Valid UserID"+"\n" +
                                            "Thank You", Toast.LENGTH_LONG).show();
                            dialog.hide();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "No Alarm Found", Toast.LENGTH_SHORT).show();
                                dialog.hide();

                        }
                    });
                }

            }
        });
        builder.setView(view2).setIcon(R.drawable.shahin).show();


    }

    public void examsetbutton(View view) {
        View setview=getLayoutInflater().inflate(R.layout.examset,null);
        AlertDialog.Builder setbuilder=new AlertDialog.Builder(this);
        datePicker=setview.findViewById(R.id.datepicker);
        timePicker=setview.findViewById(R.id.timepick);
        final EditText description;
        final AutoCompleteTextView examid;
        Button setbut;
        setbut =setview.findViewById(R.id.sete);
        description=setview.findViewById(R.id.exxamdes);
        examid=setview.findViewById(R.id.setcourseEX);
        ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList1);
        examid.setThreshold(1);
        examid.setAdapter(stringArrayAdapter);
        setbut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String setid=examid.getText().toString();
                String setuserid="e".concat(setid.concat(firebaseAuth.getCurrentUser().getEmail().substring(0,10)));
                day=datePicker.getDayOfMonth();
                month=datePicker.getMonth();
                year=datePicker.getYear();
                String d,y,m;
                d=String.valueOf(day);
                m=String.valueOf(month);
                y=String.valueOf(year);
                examhour=timePicker.getHour();
                exammin=timePicker.getMinute();
                final String examdes=description.getText().toString();
                String h=String.valueOf(examhour);
                String em=String.valueOf(exammin);
                final String date=d.concat(":").concat(m).concat(":").concat(y);
                final String time=h.concat(":").concat(em);
                ExamSchedule examSchedule=new ExamSchedule(date,time,examdes);
                DatabaseReference examdata=FirebaseDatabase.getInstance().getReference(setuserid);
                examdata.setValue(examSchedule).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Checking!");
                            builder.setMessage("Date: "+date+"\n"+"Time: "+time+"\n"+examdes);
                            builder.show();
                            Toast.makeText(MainActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        setbuilder.setView(setview).show();

    }

    public void getexambutton(View view) {
       AlertDialog.Builder getexambuilder=new AlertDialog.Builder(this);
        LayoutInflater inflater= LayoutInflater.from(MainActivity.this);
        final View examview=inflater.inflate(R.layout.get,null);
        final AutoCompleteTextView eauto;
        Button getexambut;
        eauto=examview.findViewById(R.id.getcode);
        getexambut=examview.findViewById(R.id.getbut);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        eauto.setThreshold(1);
        eauto.setAdapter(adapter);
        getexambut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getexamcode=eauto.getText().toString();
                if(getexamcode.isEmpty()){
                    eauto.setError("Enter a valid USERID");
                }else {

                    Cursor d=mydatabaseHelper.search(getexamcode);
                    if(d.getCount()==0){
                        insertmethod(getexamcode);
                    }
                    String a="e";
                    String queryexam=a.concat(getexamcode);
                    final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Finding an Exam....");
                    dialog.show();
                    final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(queryexam);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ExamSchedule examSchedule=dataSnapshot.getValue(ExamSchedule.class);
                            if(dataSnapshot.exists()){
                                dialog.hide();
                                AlertDialog.Builder showbuilder=new AlertDialog.Builder(MainActivity.this);
                                showbuilder.setTitle("Exam Schedule!!");
                                showbuilder.setMessage("Course Code: "+getexamcode.substring(0,4)+"\n"+examSchedule.getDate()+"\n"+examSchedule.getTime()+"\n"+examSchedule.getExamdescription()).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "No Exam is found!!", Toast.LENGTH_SHORT).show();
                            dialog.hide();
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, "No Alarm Found", Toast.LENGTH_SHORT).show();
                            dialog.hide();

                        }
                    });
                }

            }
        });
        getexambuilder.setView(examview).setIcon(R.drawable.shahin).show();

    }
}
