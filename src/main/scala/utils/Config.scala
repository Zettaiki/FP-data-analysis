package utils

object Config {
  // The local directory containing this repository
  val projectDir :String = "<project_directory_path>"
  // The name of your bucket on AWS S3
  val s3bucketName :String = "<bucket_name>"
  // The path to the credentials file for AWS
  val credentialsPath :String = projectDir +  "/src/main/res/aws_credentials.txt"
  // Path to the datasets directory
  val path_datasets :String = projectDir + "/datasets"
  // Path to the dataset of itineraries
  val path_dataset_itineraries :String = path_datasets + "/itineraries.csv" // or "itineraries_sample.csv" for local testing
  // Path to output directory
  val path_output :String = projectDir + "/src/main/outputs"
}
