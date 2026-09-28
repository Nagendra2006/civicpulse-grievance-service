from azure.storage.blob import BlobServiceClient
import os

connection_string = "DefaultEndpointsProtocol=https;AccountName=musicstream;AccountKey=FYKTDaRgzmwrlSfxuYRezlC6R9/4Pf59OpWnWuLFY2fbTAz/yPLuQc+N2usTFV2AuOWQiY7UWTDd+AStIurn9Q==;EndpointSuffix=core.windows.net"
container_name = "civicpulse-images"

try:
    blob_service_client = BlobServiceClient.from_connection_string(connection_string)
    container_client = blob_service_client.get_container_client(container_name)
    if not container_client.exists():
        print("Container does not exist, creating...")
        container_client.create_container()
    print("Azure connection successful!")
except Exception as e:
    print(f"Azure connection failed: {e}")
