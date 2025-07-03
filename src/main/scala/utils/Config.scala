package utils

object Config {

  // The local directory containing this repository
  val projectDir :String = "<project_dir>"
  // The name of your bucket on AWS S3
  val s3bucketName :String = "<bucket_name>"
  // The path to the credentials file for AWS (if you follow instructions, this should not be updated)
  val credentialsPath :String = "/aws_credentials.txt"

}
