# Use a base image with Python
FROM python:3.10-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the test script into the container
COPY test_cicd_jobs.py /app/

# Install the necessary dependencies
RUN pip install --no-cache-dir pytest requests

# Command to run the tests
CMD ["pytest", "test_cicd_jobs.py"]
