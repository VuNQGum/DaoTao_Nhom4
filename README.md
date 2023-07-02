# DaoTao_Nhom4

## I. Using Docker
### 1. Dockerfile
Tạo file tên Dockerfile trong thư mục project (daotao)
Cấu hình Dockerfile:
![image](https://user-images.githubusercontent.com/76396786/232329395-a5f084f9-e397-4182-8761-64db72694323.png)

Build docker image:

    docker build . --file Dockerfile --tag $DOCKER_USER/$IMAGE_NAME:$IMAGE_TAG

### 2. Docker Compose
Công cụ dùng để định nghĩa và run multi-container cho Docker application

Cấu hình trong file docker-compose.yaml:

![image](https://github.com/VuNQGum/DaoTao_Nhom4/assets/76396786/130faf00-4e78-4c73-b24e-926626bd4553)

Tạo và chạy các thành phần định nghĩa trong docker-compose.yml:
    
    docker-compose up

Dừng và xóa: image, container, mạng, đĩa tạo ra bởi docker-compose up:

    docker-compose down

### 3. Docker Hub
Docker Hub là một dịch vụ do Docker cung cấp, cho phép tìm kiếm và chia sẻ các container images tương tự như github.

Đăng nhập DockerHub:

    docker login -u $DOCKER_HUB_USER -p $DOCKER_PASSWORD

Sau khi build 1 image mới, bạn có thể đẩy lên Docker Hub để lưu trữ và chia sẻ:

    docker push $DOCKER_HUB_USER/$IMAGE_NAME:$TAG

Pull 1 image trên Docker Hub:

    docker pull $DOCKER_HUB_USER/$IMAGE_NAME:$TAG

# II.Kubernetes
Kubernetes hoặc k8s là một nền tảng mã nguồn mở giúp tự động hóa việc quản lý, mở rộng và triển khai ứng dụng dưới dạng container.

Trong đồ án lần này, nhóm sử dụng kubernetes để triển khai Docker application và postgres database.
Việc sử dụng kubernetes giúp tạo ra các bản sao giúp đảm bảo cân bằng tải và khả năng chịu lỗi.
Đồng thời việc sử dụng kubernetes giúp cho CD dễ dàng và giảm downtime khi sử dụng phương pháp Rolling Update.

Việc cấu hình kubernetes sẽ sử dụng 2 file trong folder kubernetes: daotao.yml và database.yml

Nhóm sử dụng minikube là công cụ chạy kubernetes cluster chỉ gồm một node để thực hiện và kubectl (công cụ command-line cho kubernetes) :

    minikube start # Chạy minikube
    
    kubectl apply -f kubernetes # apply tất cả file cấu hình kubernetes trong folder kubernetes

    kubectl get all -owide # HIển thị thông tin các pod, service, deployment,...
    
# III. Github Actions
Github Actions cho phép chúng ta tạo workflows vòng đời phát triển phần mềm cho dự án trực tiếp trên Github repository của chúng ta.

Với Github Actions chúng ta có thể tích hợp continuous integration (CI) và continuous deployment (CD) trực tiếp trên repository của mình.

Cấu hình Github Actions:

    - C1: Tạo file name.yml trong folder .github/workflows
    
    - C2: Tạo workflow sử dụng chức năng Actions trên repository
    
Cấu trúc file cấu hình .yml:

![image](https://github.com/VuNQGum/DaoTao_Nhom4/assets/76396786/07aaeb41-415c-46c6-aee4-51c4d753423d)

Trong đó: 

    - name: tên workflows.

    - on: định nghĩa các sự kiện kích hoạt workflow như push, pull request, workflow_dispatch (tự kích hoạt thủ công), ...

    - jobs: phân chia các công việc trong 1 workflow.

    - steps: từng bước cần thực hiện trong 1 job.

Workflow sẽ được thực thi khi có các sự kiện kích hoạt giúp thực hiện build chương trình, tạo docker image, push lên docker hub và sử dụng nó cho việc deploy.
# IV. Deploy Server
1. Tạo AWS EC2 với các config sau 
{
  "MaxCount": 1,
  "MinCount": 1,
  "ImageId": "ami-05ffd9ad4ddd0d6e2",
  "InstanceType": "t2.micro",
  "KeyName": "chuyende",
  "EbsOptimized": false,
  "BlockDeviceMappings": [
    {
      "DeviceName": "/dev/xvda",
      "Ebs": {
        "Encrypted": false,
        "DeleteOnTermination": true,
        "SnapshotId": "snap-0a2f3523aee5d97d8",
        "VolumeSize": 10,
        "VolumeType": "gp2"
      }
    }
  ],
  "NetworkInterfaces": [
    {
      "AssociatePublicIpAddress": true,
      "DeviceIndex": 0,
      "Groups": [
        "<groupId of the new security group created below>"
      ]
    }
  ],
  "TagSpecifications": [
    {
      "ResourceType": "instance",
      "Tags": [
        {
          "Key": "Name",
          "Value": "chuyende"
        }
      ]
    }
  ],
  "PrivateDnsNameOptions": {
    "HostnameType": "ip-name",
    "EnableResourceNameDnsARecord": true,
    "EnableResourceNameDnsAAAARecord": false
  }
}
2. Mua tên miền , cấu hình DNS để trỏ tên miền về public IPv4 address của server
![image](https://github.com/VuNQGum/DaoTao_Nhom4/assets/94282822/286d821d-0864-47d7-9664-eda99079b00f)

3. Cấu hình security cho phép HTTP traffic từ internet 
![image](https://github.com/VuNQGum/DaoTao_Nhom4/assets/94282822/6f6c0c89-0984-4f96-9f67-fd4b307bd478)
4. Cài đặt runtime environment server: open-jdk,git,docker,nginx , docker -compose ,
 Cấu hình nginx để chuyển tiếp request vào cổng chạy server (3000):
Sửa trong file etc/nginx/ngix.config thay thế khối server trong khối http 
server {
    listen 80;
    server_name _;

    location / {
        proxy_pass http://localhost:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}


