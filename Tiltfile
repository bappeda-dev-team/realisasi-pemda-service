# Build
custom_build(
    # Name of the container image
    ref = 'realisasi-pemda-service',

    # Command to build the container image
    command = './gradlew bootBuildImage --imageName $EXPECTED_REF',

    # Files to watch that trigger a new build
    deps = ['build.gradle', 'src'],
    disable_push=True,
    tag='dev'
)

# Deploy
k8s_yaml(kustomize('k8s'))

# Manage
k8s_resource('realisasi-pemda-service', port_forwards=['9001'])