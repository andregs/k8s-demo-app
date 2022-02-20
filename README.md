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
11. As an alternative to port-forwarding, change service type to LoadBalancer to expose it:
12. ```
    kubectl.exe expose deployment k8s-demo-app  --name=k8s-demo-app --type=LoadBalancer --port=80 --target-port=8080 --dry-run=client -o yaml > .\k8s\service.yaml
    ```
13. Cloud providers assign an external IP to services, but in local dev cluster we rely on minikube.
14. Update application.yaml & deployment.yaml to add graceful shutdown & health probes.
15. build, push, re-deploy and test the changes:
16. ```
    .\gradlew bootBuildImage
    docker push andregs/sandbox:k8s-demo-app
    kubectl.exe apply -f ./k8s
    minikube.exe service --url k8s-demo-app
    ```
17. You can test the graceful shutdown by following the logs while deleting the running pod:
18. ```
    kubectl.exe logs k8s-demo-app-6b48b7f457-26v7n -f
    kubectl.exe delete pod k8s-demo-app-6b48b7f457-26v7n
    ```

### skaffold & kustomize

1. Clean-up our stuff: `kubectl delete -f ./k8s`
2. 