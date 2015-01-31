package contactmanager.intracode.org.contactmanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    //ArrayAdapter<Contact> adapter = new ContactListAdapter();
    ArrayAdapter<Contact> adapter;
    ImageView userImgView;
    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt = (EditText)findViewById(R.id.txtName);
        phoneTxt = (EditText)findViewById(R.id.txtPhone);
        emailTxt = (EditText)findViewById(R.id.txtEmail);
        addressTxt = (EditText)findViewById(R.id.txtAddress);
        contactListView = (ListView)findViewById(R.id.listView);
        TabHost tabhost = (TabHost)findViewById(R.id.tabHost);
        //contactListView.setAdapter(adapter);
        tabhost.setup();
        userImgView = (ImageView) findViewById(R.id.imageViewUser);
        TabHost.TabSpec tabSpec = tabhost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabhost.addTab(tabSpec);

        tabSpec = tabhost.newTabSpec("List");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("Contact List");
        tabhost.addTab(tabSpec);

        final Button addBtn = (Button)findViewById(R.id.btnAdd);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact(nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addressTxt.getText().toString(), imageUri);
                //populateList();
                Toast.makeText(getApplicationContext(),nameTxt.getText().toString() + " has been added to your contact!",Toast.LENGTH_SHORT).show();
            }
        });

        userImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBtn.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        populateList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if (requestCode == 1) {
                userImgView.setImageURI(data.getData());
                imageUri = data.getData();
            }
        }
    }

    private void populateList(){
        adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private void addContact(String name, String phone, String email, String address, Uri imageUri){
        Contacts.add(new Contact(name, phone, email, address, imageUri));
        adapter.notifyDataSetChanged();
    }

    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter(){
            super(MainActivity.this, R.layout.listview_item, Contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Contact currentContact = Contacts.get(position);
            TextView name = (TextView)view.findViewById(R.id.cName);
            name.setText(currentContact.getName());
            TextView phone = (TextView)view.findViewById(R.id.cPhone);
            phone.setText(currentContact.getPhone());
            TextView email = (TextView)view.findViewById(R.id.cEmail);
            email.setText(currentContact.getEmail());
            TextView address = (TextView)view.findViewById(R.id.cAddress);
            address.setText(currentContact.getAddress());
            ImageView ivContact = (ImageView)view.findViewById(R.id.imgViewFriends);
            ivContact.setImageURI(currentContact.getImageUri());

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
