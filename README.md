# README
## Spring on Kubernetes!

Tutorial from
https://hackmd.io/@ryanjbaxter/spring-on-k8s-workshop

1. Generated from [start.spring.io](https://start.spring.io/#!type=gradle-project&language=kotlin&platformVersion=2.6.3&packaging=jar&jvmVersion=17&groupId=com.example&artifactId=k8s-demo-app&name=k8s-demo-app&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.demo&dependencies=web,actuator) with web & actuator.
2. Create a Hello controller.
3. Customize the generated docker image name in gradle config.
4. Build the app and publish it to Docker Hub:
5. ```shell
   .\gradlew bootBuildImage
   docker push andregs/sandbox:k8s-demo-app
   ```
6. Deploy to kubernetes:
7. ```shell
   mkdir k8s
   kubectl.exe create deployment k8s-demo-app --dry-run=client -o yaml --image=andregs/sandbox:k8s-demo-app > k8s/deployment.yaml
   kubectl.exe create service clusterip k8s-demo-app --tcp=80:8080 -o yaml --dry-run=client > k8s/service.yaml
   minikube start
   kubectl.exe apply -f ./k8s
   kubectl.exe rollout status deployment k8s-demo-app
   ```
8. At this point the app is running, but it's accessible inside the cluster network only.
9. Forward a port to the service and test it on your browser:
10. ```shell
    kubectl.exe port-forward service/k8s-demo-app 8080:80
    ```
11. 