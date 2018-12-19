job "callme-service" {
	datacenters = ["dc1"]
	type = "service"
	group "callme" {
		count = 1
		task "api" {
			driver = "java"
			config {
				jar_path    = "C:\\Users\\minkowp\\git\\sample-nomad-java-services-idea\\callme-service\\target\\callme-service-1.0.0-SNAPSHOT.jar"
				jvm_options = ["-Xmx256m", "-Xms128m"]
			}
			env {
				VAULT_TOKEN_2 = "s.zPjMgGjdskZdAm3fbn9HmonO"
			}
			resources {
				cpu    = 500 # MHz
				memory = 300 # MB
				network {
					port "http" {}
				}
			}
			service {
				name = "callme-service"
				port = "http"
			}
			vault {
				policies = ["default"]
			}
		}
		restart {
			attempts = 1
		}
	}
}