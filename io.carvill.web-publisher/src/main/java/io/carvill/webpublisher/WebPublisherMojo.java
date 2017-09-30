package io.carvill.webpublisher;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.StorageClass;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
@Mojo(name = "maven-aws-webpublisher", defaultPhase = LifecyclePhase.INSTALL)
public class WebPublisherMojo extends AbstractMojo {

    @Parameter(property = "aws-access-key", required = true)
    private String awsAccessKey;

    @Parameter(property = "aws-secret-key", required = true)
    private String awsSecretKey;

    @Parameter(property = "aws-region", required = true)
    private String awsRegion;

    @Parameter(property = "aws-bucket", required = true)
    private String awsBucket;

    @Parameter(property = "source-folder-file", required = true)
    private File sourceFolderFile;

    @Parameter(property = "destination-folder", required = true)
    private String destinationFolder;

    @Parameter(property = "file-extension-filter")
    private String fileExtensionFilter;

    @Parameter(property = "file-content-type")
    private String fileContentType;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        this.getLog().info("Source: " + this.sourceFolderFile);
        if (this.sourceFolderFile == null || !this.sourceFolderFile.isDirectory()) {
            throw new MojoFailureException("Source folder parameter is not a valid folder");
        }

        final AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(this.awsAccessKey, this.awsSecretKey));
        final AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider);
        if (StringUtils.isNotBlank(this.awsRegion)) {
            try {
                builder.withRegion(this.awsRegion);
            } catch (final RuntimeException e) {
                this.getLog().warn("Region parameter '" + this.awsRegion + "' is invalid: " + e.getMessage());
            }
        }

        final AmazonS3 amazonS3 = builder.build();
        final File[] list;
        if (StringUtils.isBlank(this.fileExtensionFilter)) {
            list = this.sourceFolderFile.listFiles();
        } else {
            list = this.sourceFolderFile.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(final File dir, final String name) {
                    return StringUtils.endsWithIgnoreCase(name, WebPublisherMojo.this.fileExtensionFilter);
                }

            });
        }

        if (ArrayUtils.isEmpty(list)) {
            this.getLog().info("There are not files to process");
            return;
        }

        this.getLog().info("Processing '" + list.length + "' files from " + this.sourceFolderFile.getAbsolutePath()
                + " [filter: " + this.fileExtensionFilter + "]");
        for (final File file : list) {
            final String key = String.format("%s/%s", this.destinationFolder, file.getName());
            final ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.length());
            meta.setCacheControl("public, max-age=31536000");
            if (StringUtils.isNotBlank(this.fileContentType)) {
                meta.setContentType(this.fileContentType);
            }

            final PutObjectRequest s3Request = new PutObjectRequest(this.awsBucket, key, file);
            s3Request.setStorageClass(StorageClass.Standard);
            s3Request.setCannedAcl(CannedAccessControlList.PublicRead);
            s3Request.setMetadata(meta);

            try {
                final PutObjectResult s3Response = amazonS3.putObject(s3Request);
                this.getLog().info("File " + file.getName() + " was uploaded with MD5 " + s3Response.getContentMd5());
            } catch (final AmazonClientException e) {
                throw new MojoFailureException("File " + file.getName() + " can not be uploaded", e);
            }
        }
    }

}
