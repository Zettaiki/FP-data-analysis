# FP-data-analysis
*Big Data analysis on flight prices data.*

This project is designed to analyze flight prices data, particularly focusing on
the following jobs:

- **Average Flight Price per Route with Classification:** Calculate the average ticket price for each departure-destination pair, then classify each route as "Cheap", "Moderate", or "Expensive" based on the calculated average price.
- **Direct vs. Connecting Flights Price Comparison:**  Compare average ticket prices between non-stop and connecting flights on each route to analyze fare differences by flight type.

The project will focus in implementing these jobs using a non-optimized and an optimized approach.

The dataset used in this project is sourced from Kaggle, [Flight Prices by Dillon Wong](https://www.kaggle.com/datasets/dilwong/flightprices).

## Requirements

- Python 3.11 (with Jupyter Notebook)
- Java 11
- Apache Spark 3.5.1
- Scala
- *(Optional)* Setup for Kaggle API to download the dataset automatically
- *(Optional)* AWS CLI (for S3 access)

**Note:** The dataset is not included in the repository due to its size. You can download it from Kaggle and place it in the `datasets/` directory, or use the Kaggle API to download it automatically.

## Directory structure

The project is structured as follows:

```
src
`-- main
    |-- outputs
    |   |-- non_optimized_job.log
    |   `-- optimized_job.log
    |-- python
    |   `-- DatasetAnalysis.ipynb                   # Jupyter Notebook for data analysis
    |-- res
    |   `-- aws_credentials.txt
    `-- scala
        |-- app
        |   |-- Flight.scala
        |   |-- FlightPricesAnalysisApp.scala       # Main application entry point
        |   `-- ItinerariesParser.scala
        |-- debug
        |   `-- DebugNotebook.ipynb                 # Debugging Jupyter Notebook with spylon-kernel
        `-- utils
            |-- Commons.scala
            `-- Config.scala                        # Configuration of paths for project
```

## Setup and Run

### File `DatasetAnalysis.ipynb`
- This jupyter notebook is used for data analysis and visualization.
- It already contains the code to install the pip packages required for the analysis and the dataset.
- At the end of the notebook, you can find the code to save the results and generate samples in a CSV file, which will be used by the Scala application.

### File `DebugNotebook.ipynb`
- This Jupyter notebook is used for debugging purposes.
- Execute the following commands in the terminal to set up the environment for debugging:
```bash
> pip install findspark
> pip install spylon-kernel
> python -m spylon_kernel install
```
- After setting up the environment, you can run the notebook to debug the Scala code using the Spark context.
- This notebook contains the same code as the main application, but it is designed to be run in a Jupyter notebook environment for debugging purposes.

### File `FlightPricesAnalysisApp.scala`
- This is the main application Scala code.
- Before running the application, ensure to set the correct paths in `Config.scala` for the dataset and output directories.
- To run the application (after building), you can use the following commands in the terminal:
```
FP-data-analysis.jar    non-opt     # Run the non-optimized job
                        opt         # Run the optimized job
                        both        # Run both jobs
```

## License

This project is licensed under the MIT License – see the [LICENSE](./LICENSE) file for details.

The MIT License is a simple and permissive license that allows users to:

- Use the software for any purpose
- Modify the software to suit their needs
- Distribute copies of the software
- Include the software in proprietary projects

The software is provided "as is", without any warranty.

For more information about the MIT License, visit:  
https://opensource.org/licenses/MIT
