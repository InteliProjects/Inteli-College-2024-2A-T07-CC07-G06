import json
import boto3
from datetime import datetime, timedelta

def lambda_handler(event, context):
    ec2_client = boto3.client('ec2')
    cloudwatch_client = boto3.client('cloudwatch')
    
    instance_id = event.get('instanceId', None)
    
    if not instance_id:
        return {
            'statusCode': 400,
            'body': json.dumps('Faltando o ID da instância.')
        }
    
    try:
        ec2_response = ec2_client.describe_instance_status(InstanceIds=[instance_id])
        instance_status = ec2_response['InstanceStatuses']
        
        if not instance_status:
            return {
                'statusCode': 404,
                'body': json.dumps(f'Instância {instance_id} não encontrada ou não está rodando.')
            }
        
        state = instance_status[0]['InstanceState']['Name']
        system_status = instance_status[0]['SystemStatus']['Status']
        instance_status = instance_status[0]['InstanceStatus']['Status']
        
        now = datetime.utcnow()
        start_time = now - timedelta(minutes=5)
        
        cpu_metric = cloudwatch_client.get_metric_statistics(
            Namespace='AWS/EC2',
            MetricName='CPUUtilization',
            Dimensions=[
                {'Name': 'InstanceId', 'Value': instance_id}
            ],
            StartTime=start_time,
            EndTime=now,
            Period=300,
            Statistics=['Average']
        )
        
        cpu_usage = cpu_metric['Datapoints'][0]['Average'] if cpu_metric['Datapoints'] else "No data"
        
        return {
            'statusCode': 200,
            'body': json.dumps({
                'instanceId': instance_id,
                'state': state,
                'systemStatus': system_status,
                'instanceStatus': instance_status,
                'cpuUtilization': cpu_usage
            })
        }

    except Exception as e:
        return {
            'statusCode': 500,
            'body': json.dumps(f'Erro ao consultar a instância EC2: {str(e)}')
        }
