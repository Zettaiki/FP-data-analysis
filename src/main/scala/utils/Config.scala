package utils

object Config {
  // The local directory containing this repository
  val projectDir :String = "<project_dir>"
  // The name of your bucket on AWS S3
  val s3bucketName :String = "<bucket_name>"
  // The path to the credentials file for AWS
  val credentialsPath :String = projectDir +  "/src/main/res/aws_credentials.txt"
  // Path to the dataset of itineraries
  val path_dataset_itineraries :String = projectDir + "/datasets/itineraries.csv"
}
