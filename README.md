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