{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "chat-service.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "chat-service.fullname" -}}
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
Create chart name and version as used by the chart label.
*/}}
{{- define "chat-service.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/* Get the service hostname for this chart */}}
{{- define "chat-service.hostname" -}}
{{ include "chat-service.name" . }}.{{ .Release.Namespace }}.svc.cluster.local
{{- end -}}

{{- define "eureka.hostname" -}}
service-registry.{{ .Release.Namespace }}.svc.cluster.local
{{- end -}}

{{- define "mongodb.hostname" -}}
{{ .Release.Name }}-mongodb.{{ .Release.Namespace }}.svc.cluster.local
{{- end -}}

{{- define "rabbitmq.discovery.hostname" -}}
{{ .Release.Name }}-rabbitmq-ha-discovery.{{ .Release.Namespace }}.svc.cluster.local
{{- end -}}
