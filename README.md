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
   docker push andregs/k8s-demo-app
   ```
6. Deploy to kubernetes:
7. ```shell
   mkdir k8s
   kubectl.exe create deployment k8s-demo-app --dry-run=client -o yaml --image=andregs/k8s-demo-app > k8s/deployment.yaml
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
    docker push andregs/k8s-demo-app
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
2. Skaffold generates immutable tags for container images, so I cannot re-use andregs/sandbox repo.
3. Skaffold stores built image in minikube local registry, so no push/download (docker hub) is required.
4. After creating skaffold.yaml, execute `skaffold.exe dev --port-forward`
   1. I get an error at this point. I tried a bleeding edge version to avoid https://github.com/GoogleContainerTools/skaffold/issues/6948 but the error persist:
   2. See skaffold.yaml for more details.

```
[exporter] no default process type
[exporter] Saving andregs/k8s-demo-app:latest...
[exporter] *** Images (32e7b2810aeb):
[exporter]       andregs/k8s-demo-app:latest
[exporter] Reusing cache layer 'paketo-buildpacks/bellsoft-liberica:jdk'
[exporter] Reusing cache layer 'paketo-buildpacks/syft:syft'
[exporter] Adding cache layer 'paketo-buildpacks/gradle:application'
[exporter] Adding cache layer 'paketo-buildpacks/gradle:cache'
[exporter] Reusing cache layer 'cache.sbom'
Build [andregs/k8s-demo-app] succeeded
Tags used in deployment:
- andregs/k8s-demo-app -> andregs/k8s-demo-app:32e7b2810aeb1b42d21fe5eb54cd03a291b8f7b0496ac9cf4420f9692b80a113
  Starting deploy...
- deployment.apps/k8s-demo-app created
- service/k8s-demo-app created
  Waiting for deployments to stabilize...
- deployment/k8s-demo-app: container k8s-demo-app terminated with exit code 82
    - pod/k8s-demo-app-8674d764cc-p9nsw: container k8s-demo-app terminated with exit code 82
      > [k8s-demo-app-8674d764cc-p9nsw k8s-demo-app] ERROR: failed to launch: determine start command: when there is no default process a command is required
- deployment/k8s-demo-app failed. Error: container k8s-demo-app terminated with exit code 82.
```
