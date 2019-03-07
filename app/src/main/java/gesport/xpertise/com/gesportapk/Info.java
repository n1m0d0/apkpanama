package gesport.xpertise.com.gesportapk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Info extends AppCompatActivity {


    TextView user;
    TextView version;
    String auth;
    String userName;
    String versionGes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle parametros = this.getIntent().getExtras();
        auth = parametros.getString("auth");
        userName = parametros.getString("userName");
        user = findViewById(R.id.user);
        try {
            versionGes = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        user.setText(userName);
        version = findViewById(R.id.version);
        version.setText("Version " +versionGes);
    }
    public void logo(View v) {
        Uri uri = Uri.parse("https://www.portcolon2000.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void gesport(View v) {
        Uri uri = Uri.parse("https://www.xpertise-pa.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void privacidad(View v) {
        Uri uri = Uri.parse("https://www.portcolon2000.site/politica/politica_privacidad.pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
