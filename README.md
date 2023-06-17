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
# III. Github Actions
# IV. Deploy Server