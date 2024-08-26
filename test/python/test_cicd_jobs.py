import logging
import requests
import pytest
import json
import datetime
import os
import time

# Global variable to control the deletion of all current jobs after test.
DELETE_ALL_JOBS = False

# Create a log directory if it doesn't exist
log_directory = os.path.join(os.path.dirname(os.path.abspath(__file__)), "log")
if not os.path.exists(log_directory):
    os.makedirs(log_directory)

# Configure logging
now = datetime.datetime.now()
filename = f"{log_directory}/Test_{now.strftime('%Y-%m-%d_%H-%M-%S')}.txt"
logging.basicConfig(filename=filename, level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# Console logging
console_handler = logging.StreamHandler()
console_handler.setLevel(logging.INFO)
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
console_handler.setFormatter(formatter)
logging.getLogger().addHandler(console_handler)

# CI/CD Server Base URL
BASE_URL = "http://cd-server:8080/api"

# Retry Configuration
MAX_RETRIES = 5
RETRY_DELAY = 5  # seconds

# Function to log HTTP requests
def log_request(response):
    logger.info("\n" + "=" * 70)
    logger.info(f"Request {response.request.method} to {response.request.url}")

    if response.request.body:
        logger.info(f"Request Body:\n{response.request.body.decode('utf-8')}\n")

    logger.info(f"Response Status Code: {response.status_code}")

    try:
        response_body = response.json()
        logger.info(f"Response Body:\n{json.dumps(response_body, indent=4)}\n")
    except json.JSONDecodeError:
        logger.warning("Response is not in valid JSON format or is empty.")
        logger.info(f"Response Body:\n{response.text}\n")

    logger.info("=" * 70)

# Function to log test results
def log_test_result(test_name, status):
    result_message = "***PASSED***" if status == "passed" else "***FAILED***"
    status_color = "\033[92m" if status == "passed" else "\033[91m"
    logger.info("\n" + "-" * 65)
    logger.info(f"Test: {test_name}(), {status_color}{result_message}\033[0m")
    logger.info("-" * 65)

# Function to log HTTP responses (Part 2, point 3)
def log_response(response):
    logger.info(f"Response Status Code: {response.status_code}")
    try:
        response_body = response.json()
        logger.info(f"Response Body:\n{json.dumps(response_body, indent=4)}\n")
    except json.JSONDecodeError:
        logger.warning("Response is not in valid JSON format or is empty.")
        logger.info(f"Response Body:\n{response.text}\n")

# Function to delete all jobs
def delete_all_jobs():
    if DELETE_ALL_JOBS:
        logger.info("Attempting to delete all jobs...")
        try:
            # Fetch current jobs to delete
            response = requests.get(f"{BASE_URL}/jobs")
            log_request(response)

            if response.status_code == 200:
                jobs = response.json()
                for job in jobs:
                    job_id = job['id']
                    delete_response = requests.delete(f"{BASE_URL}/jobs/{job_id}")
                    log_response(delete_response)  # Use log_response here
                    logger.info(f"Deleted job with ID: {job_id}")
            else:
                logger.warning("Failed to retrieve jobs for deletion. Status Code: %d", response.status_code)

        except requests.exceptions.RequestException as e:
            logger.error("An error occurred while deleting jobs: %s", e)
    else:
        logger.info("Global flag DELETE_ALL_JOBS is set to False. No jobs will be deleted.")

# Define a fixture to create a new job before testing (Part 2, point 4)
@pytest.fixture(scope='module')  # Define scope as 'module'
def create_job():
    logger.info("Setting up a new job for tests...")  # Log setup action
    job_data = {
        "jobName": "Test Job",
        "jobType": "Build",
        "status": "PENDING"
    }
    response = requests.post(f"{BASE_URL}/jobs", json=job_data)
    log_request(response)
    assert response.status_code == 201, "Job creation failed."
    job_id = response.json()["id"]
    yield job_id  # Yield the job ID to the test functions
    logger.info("Tearing down the job after tests...")  # Log teardown action

    # Now delete the job to clean up (optional)
    delete_response = requests.delete(f"{BASE_URL}/jobs/{job_id}")
    log_response(delete_response)
    logger.info(f"Deleted test job with ID: {job_id}")

# Function to make a GET request with retries
def get_with_retries(url):
    for i in range(MAX_RETRIES):
        try:
            response = requests.get(url)
            if response.status_code == 200:
                return response
        except requests.exceptions.ConnectionError:
            if i < MAX_RETRIES - 1:
                time.sleep(RETRY_DELAY)
            else:
                raise
    return None

# Enhanced Test Functions

def test_get_all_jobs():
    test_name = "test_get_all_jobs"
    try:
        response = get_with_retries(f"{BASE_URL}/jobs")
        assert response is not None, "Failed to connect to cd-server"
        log_request(response)
        assert response.status_code == 200, "Failed to get all jobs."
        assert isinstance(response.json(), list), "Response is not a list."
        log_test_result(test_name, "passed")
    except AssertionError:
        log_test_result(test_name, "failed")

def test_get_job_by_id(create_job):
    test_name = "test_get_job_by_id"
    try:
        response = get_with_retries(f"{BASE_URL}/jobs/{create_job}")
        assert response is not None, "Failed to connect to cd-server"
        log_request(response)
        assert response.status_code == 200, f"Failed to get job with ID {create_job}."
        assert response.json()["id"] == create_job, "Incorrect job ID."
        log_test_result(test_name, "passed")
    except AssertionError:
        log_test_result(test_name, "failed")

def test_create_job():
    test_name = "test_create_job"
    try:
        job_data = {
            "jobName": "Another Test Job",
            "jobType": "Deployment",
            "status": "PENDING"
        }
        response = requests.post(f"{BASE_URL}/jobs", json=job_data)
        log_request(response)
        assert response.status_code == 201, "Job creation failed."
        assert response.json()["jobName"] == "Another Test Job", "Incorrect job name."
        log_test_result(test_name, "passed")
    except AssertionError:
        log_test_result(test_name, "failed")

def test_update_job(create_job):
    test_name = "test_update_job"
    try:
        update_data = {
            "jobName": "Test Job",
            "jobType": "Build",
            "status": "Running"
        }
        response = requests.put(f"{BASE_URL}/jobs/{create_job}", json=update_data)
        log_request(response)
        assert response is not None, "Failed to connect to cd-server"
        assert response.status_code == 200, f"Failed to update job with ID {create_job}."
        assert response.json()["status"] == "Running", "Job status not updated."
        log_test_result(test_name, "passed")
    except AssertionError:
        log_test_result(test_name, "failed")

def test_delete_job(create_job):
    test_name = "test_delete_job"
    try:
        response = requests.delete(f"{BASE_URL}/jobs/{create_job}")
        log_response(response)  # Log response using log_response function
        assert response is not None, "Failed to connect to cd-server"
        assert response.status_code == 204, f"Failed to delete job with ID {create_job}."

        response = requests.get(f"{BASE_URL}/jobs/{create_job}")
        log_request(response)
        assert response.status_code == 404, f"Deleted job was still found. Response Body: {response.text}"
        log_test_result(test_name, "passed")
    except AssertionError:
        log_test_result(test_name, "failed")

# Delete all jobs function will execute only if: DELETE_ALL_JOBS = True
delete_all_jobs()

# Run tests using pytest
if __name__ == "__main__":
    pytest.main()
