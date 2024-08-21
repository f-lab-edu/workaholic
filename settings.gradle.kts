rootProject.name = "workaholic"
include("kubernetes")
include("vcs-integration")
include("gateway")
include("datasource:datasource-work")
include("datasource:datasource-pod")
include("datasource:datasource-transaction")
include("mq-error")
include("rabbitmq:mq-vcs")
