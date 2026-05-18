.PHONY: yamllint hadolint shellcheck ci help lock-apk-versions trivy docker-mirror

yamllint: ## Lint YAML files
	docker run --rm -v $(CURDIR):/workdir -w /workdir cytopia/yamllint:1 .

hadolint: ## Lint Dockerfiles
	docker run --rm -v $(CURDIR):/workdir -w /workdir hadolint/hadolint:v2.14.0-alpine sh -c "hadolint --failure-threshold=error \$$(find . -name 'Dockerfile*' -not -path './.git/*')"

shellcheck: ## Lint shell scripts
	find . -not -path './.git/*' -name '*.sh' -exec docker run --rm -v $(CURDIR):/workdir -w /workdir koalaman/shellcheck-alpine:v0.11.0 shellcheck --severity=warning {} +



lock-apk-versions: ## Lock apk package versions in Dockerfiles
	./scripts/lock-apk-versions.sh
trivy: ## Scan with Trivy
	docker run --rm -v $(CURDIR):/workdir -w /workdir -v trivy_cache:/root/.cache/trivy aquasec/trivy:0.70.0 fs --severity HIGH,CRITICAL .

ci: yamllint hadolint shellcheck trivy ## Run CI pipeline locally
	woodpecker exec --backend-engine docker --repo-trusted-volumes --pipeline-event push --backend-docker-network bridge --plugins-privileged woodpeckerci/plugin-docker-buildx --repo-path "$$PWD" .woodpecker/build.yaml

docker-mirror: ## Configure Docker daemon registry mirror via Nexus
	@echo "Configuring Docker daemon registry mirror..."
	@sudo python3 -c "import json, os; path = '/etc/docker/daemon.json'; config = json.load(open(path)) if os.path.exists(path) else {}; mirrors = config.get('registry-mirrors', []); url = 'http://192.168.0.18:8090/repository/docker-hub-proxy/'; [mirrors.append(url) for _ in [1] if url not in mirrors]; config['registry-mirrors'] = mirrors; json.dump(config, open(path, 'w'), indent=2)"
	@sudo systemctl restart docker
	@echo "Docker mirror configured. Run 'docker info' to verify."

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## ' $(MAKEFILE_LIST) | sort | \
	awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-15s\033[0m %s\n", $$1, $$2}'
