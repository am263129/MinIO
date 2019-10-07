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

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.errors.RegionConflictException;

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
//                targetIP = targetip.getText().toString();
                /*
                AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

                ClientConfiguration clientConfig = new ClientConfiguration();
                clientConfig.setProtocol(Protocol.HTTPS);

                AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                        "https://10.3.1.124:9000", "us-west-1");

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
                /*
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
                client.putObject("upload_csv_minio", "cat.jpg", tempFile.toFile());
                */
                MinioClient minioClient = null;
                try {
                    minioClient = new MinioClient("http://10.3.1.124:9000", accessKey,
                            secretKey);
                } catch (InvalidEndpointException e) {
                    e.printStackTrace();
                } catch (InvalidPortException e) {
                    e.printStackTrace();
                }
                boolean isExist = false;
                try {
                    isExist = minioClient.bucketExists("upload_csv_minio");
                } catch (InvalidBucketNameException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InsufficientDataException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoResponseException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (ErrorResponseException e) {
                    e.printStackTrace();
                } catch (InternalException e) {
                    e.printStackTrace();
                }
                if (isExist) {
                    System.out.println("Bucket already exists.");
                }
                else {
                    try {
                        minioClient.makeBucket("upload_csv_minio");
                    } catch (InvalidBucketNameException e) {
                        e.printStackTrace();
                    } catch (RegionConflictException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InsufficientDataException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoResponseException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (ErrorResponseException e) {
                        e.printStackTrace();
                    } catch (InternalException e) {
                        e.printStackTrace();
                    }
                }

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

                try {
                    minioClient.putObject("upload_csv_minio", "cat.jpg", tempFile.toString());
                } catch (InvalidBucketNameException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoResponseException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (ErrorResponseException e) {
                    e.printStackTrace();
                } catch (InternalException e) {
                    e.printStackTrace();
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                } catch (InsufficientDataException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
