# templates/_helpers.tpl

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "kube-config-scanner.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Password generation function
*/}}
{{- define "gen.secret" -}}
{{- $secret := lookup "v1" "Secret" .Release.Namespace "mongo-secret" -}}
{{- if $secret -}}
{{/*
   Reuse existing password
*/}}
mongo_password: {{ $secret.data.mongo_password }}
{{- else -}}
{{/*
    Generate new password
*/}}
password: {{ randAlphaNum 10 | b64enc }}
{{- end -}}
{{- end -}}
