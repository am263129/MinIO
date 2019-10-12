package social.media.minio;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.Transfer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public String accessKey = "JqqcQzLPx9A8M6eL";
    public String secretKey = "S77TZka2CDpLWss3";
    public String targetIP = "10.3.1.124:9000";
    private static String bucketName = "testbucket";
    private static String keyName = "hosts";
    private static String uploadFileName = "/etc/hosts";
    TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText targetip = (EditText) findViewById(R.id.taraget_ip);
        Button ok = (Button) findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

    }

    private void upload() {
        final String OBJECT_KEY = "unique_id";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3 = new AmazonS3Client(credentials);
        java.security.Security.setProperty("networkaddress.cache.ttl", "60");
        s3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
        s3.setEndpoint(targetIP);
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
            Log.e("Bucket ", "Name " + bucket.getName() + " Owner " + bucket.getOwner() + " Date " + bucket.getCreationDate());
        }
        Log.e("Size ", "" + s3.listBuckets().size());
        transferUtility = new TransferUtility(s3, getApplicationContext());
        File UPLOADING_IMAGE = new File(Environment.getExternalStorageDirectory().getPath() + "/Screenshot.png");
        TransferObserver observer = transferUtility.upload(bucketName, UPLOADING_IMAGE);
        final ProgressDialog progress = new ProgressDialog(this);
        progress.show();
        observer.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                progress.hide();
                Toast.makeText(MainActivity.this, "ID " + id + "\nState " + state.name() + "\nImage ID " + OBJECT_KEY, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent / bytesTotal * 100);
//                progress.setProgress(percentage);
                //Display percentage transfered to user
            }

            @Override
            public void onError(int id, Exception ex) {
                // do something
                Log.e("Error  ", "" + ex);
            }

        });
    }
}
