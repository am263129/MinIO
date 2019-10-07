package social.media.minio;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MainActivity extends AppCompatActivity {
    public String accessKey = "JqqcQzLPx9A8M6eL";
    public String secretKey = "S77TZka2CDpLWss3";
    public String targetIP = "10.3.1.124:9000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText targetip = (EditText)findViewById(R.id.taraget_ip);
        Button ok = (Button)findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                targetIP = targetip.getText().toString();
                AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

                ClientConfiguration clientConfig = new ClientConfiguration();
                clientConfig.setProtocol(Protocol.HTTP);

                AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                        "http://10.3.1.124:9000", "us-east-1");

                AmazonS3 client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withClientConfiguration(clientConfig)
                        .withEndpointConfiguration(endpointConfiguration).build();

                boolean isExist = client.doesBucketExistV2("upload_csv_minio");
                if (isExist) {
                    System.out.println("Bucket already exists.");
                }
                else {
                    client.createBucket("upload_csv_minio");
                }
                /*
                List<Bucket> buckets = client.listBuckets();
                buckets.forEach(b -> System.out.println(b.getName()));
                */
                URL url = null;
                try {
                    url = new URL(
                            "http://www.cutestpaw.com/wp-content/uploads/2015/11/My-Cute-Baby-Cat.jpg");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Path tempFile = null;
                try {
                    tempFile = Files.createTempFile("cat", ".jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (InputStream in = url.openStream()) {
                    Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client.putObject("cataws", "cat.jpg", tempFile.toFile());
            }
        });
    }
}
