package io.carvill.awsversioner;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClientBuilder;
import com.amazonaws.services.elasticbeanstalk.model.ApplicationVersionDescription;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationVersionRequest;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationVersionResult;
import com.amazonaws.services.elasticbeanstalk.model.S3Location;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.StorageClass;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
@Mojo(name = "maven-aws-versioner", defaultPhase = LifecyclePhase.INSTALL)
public class AWSVersionerMojo extends AbstractMojo {

    @Parameter(property = "aws-access-key", required = true)
    private String awsAccessKey;

    @Parameter(property = "aws-secret-key", required = true)
    private String awsSecretKey;

    @Parameter(property = "aws-bucket")
    private String awsBucket;

    @Parameter(property = "aws-bucket-region", required = true)
    private String awsBucketRegion;

    @Parameter(property = "aws-application-name", required = false)
    private String awsApplicationName;

    @Parameter(property = "aws-application-region", required = false)
    private String awsApplicationRegion;

    @Parameter(property = "war-key", required = false)
    private String warKey;

    @Parameter(property = "war-file", required = false)
    private File warFile;

    @Parameter(property = "version", required = false)
    private String version;

    @Parameter(property = "description", required = false)
    private String description;

    @Override
    public void execute() throws MojoFailureException {
        final Log log = this.getLog();

        final AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(this.awsAccessKey, this.awsSecretKey));
        final AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider);

        final Regions bucketRegion = this.getRegion(this.awsBucketRegion, null);
        if (bucketRegion == null) {
            log.warn("Bucket region '" + this.awsBucketRegion + "' is invalid");
        } else {
            builder.withRegion(bucketRegion);
        }

        final AmazonS3 amazonS3 = builder.build();

        log.info("Pushing '" + this.warKey + "' to bucket " + this.awsBucket + " into region " + this.awsBucketRegion);
        final PutObjectRequest s3Request = new PutObjectRequest(this.awsBucket, this.warKey, this.warFile);
        s3Request.setStorageClass(StorageClass.Standard);

        try {
            final PutObjectResult s3Response = amazonS3.putObject(s3Request);
            log.info("WAR " + this.warKey + " was uploaded with MD5 " + s3Response.getContentMd5());
        } catch (final AmazonClientException e) {
            throw new MojoFailureException("WAR can not be uploaded", e);
        }

        if (StringUtils.isBlank(this.awsApplicationName)) {
            return;
        }

        final AWSElasticBeanstalkClientBuilder ebsBuilder = AWSElasticBeanstalkClientBuilder.standard()
                .withCredentials(credentialsProvider);
        final Regions applicationRegion = this.getRegion(this.awsApplicationRegion, Regions.DEFAULT_REGION);
        if (applicationRegion == null) {
            log.warn("Application region '" + this.awsApplicationRegion + "' is invalid");
        } else {
            ebsBuilder.withRegion(applicationRegion);
        }

        final AWSElasticBeanstalk ebsClient = ebsBuilder.build();
        final S3Location bundle = new S3Location(this.awsBucket, this.warKey);
        final CreateApplicationVersionRequest request = new CreateApplicationVersionRequest(this.awsApplicationName,
                this.version);
        request.setDescription(this.description);
        request.setSourceBundle(bundle);

        try {
            final CreateApplicationVersionResult result = ebsClient.createApplicationVersion(request);
            final ApplicationVersionDescription version = result.getApplicationVersion();
            log.info("Version '" + version.getVersionLabel() + "' was uploaded to application "
                    + version.getApplicationName());
        } catch (final AmazonClientException e) {
            throw new MojoFailureException("Unable to create a new version into Elastic Beanstalk", e);
        }
    }

    Regions getRegion(final String regionName, final Regions defaultRegion) {
        if (StringUtils.isNotBlank(regionName)) {
            try {
                return Regions.fromName(regionName);
            } catch (final IllegalArgumentException e) {
            }
        }

        if (defaultRegion == null) {
            return null;
        }
        return defaultRegion;
    }

}
