
# ARQUIVO DE DUMP E EXPORT POSTMAN
Afim de facilitar, além do arquivo de DUMP adicionei o export do Postman no 
diretório foursales-project3/ARQUIVOS_IMPLANTACAO

dump-foursalesdb.sql
FourSales.postman_collection.json


# CONFIGURAÇÕES DO APPLICATION.PROPERTIES
Alterar somente os campos abaixo de acordo com os servidores de aplicação de cada um


# Configuração do MySQL
spring.datasource.url=jdbc:mysql://#######:####/FOURSALESDB?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=#####
spring.datasource.password=############


#Configurações do Kafka
spring.kafka.bootstrap-servers=##############:####

# Configuração do Elasticsearch
spring.elasticsearch.uris=http://###########:####

# Configuracao de email
spring.mail.username==############
spring.mail.password==############

Obs. Na configuração de E-MAIL posso passar o meu próprio, pois para o envio através de aplicativo o GMAIL pede uma limberação


# CONFIGURAÇÕES ADICIONAIS
-------- ATUALIZAR O E-MAIL NA TABELA DE USUÁRIOS ------------
-- VOCÊ RECEBERÁ UM E-MAIL QUANDO UM PEDIDO FOR CANCELADO
UPDATE USER SET EMAIL = '##############@#######.COM' WHERE 1=1;


# ELASTICSEARCH
Para conseguir fazer a chamada no Elasticsearch pelo http sem certificado é necessário alterar a configuração do arquivo \config\elasticsearch.yml conforme abaixo:
xpack.security.enabled: false
