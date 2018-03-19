library('org.renjin.cran:e1071')
"librairie e1071 chargée!"
"On enlève les variables inutiles qui n'ont qu'une seule valeur"
csv <- csv[,which(sapply(csv, function(x) length(levels(as.factor(x)))<2)==F)]
"On regarde le nom des colonnes"
col <- colnames(csv)
"La variable à expliquer est (s'il n'y a rien après cette ligne, la variable n'existe pas, il faudra checker le csv)"
colnames(csv)[which(colnames(csv)==nomVar)]
"On change le nom de la variable à expliquer en var"
colnames(csv)[which(colnames(csv)==nomVar)]="var"
"On transforme la variable à expliquer en facteur"
csv$var <- as.factor(as.character(csv$var))
"Nombre de lignes du csv: "
total <- length(csv$var)
long_app <- floor(total*propApp)
long_test <- total-long_app
"On crée la base d'apprentissage et la base de test (70/30)"
id_app=sample(seq_len(total), size = long_app)
base_test <- csv[-id_app,]
"On démarre le svm"
sv <- svm(var~., data=csv, subset=id_app)
"On affiche les valeurs prédites"
pred <- predict(sv,base_test)
lab <- levels(as.factor(csv$var))
conf <- matrix(0, nrow = length(lab), ncol = length(lab))
for (i in 1:length(lab)){conf[i,] <- table(pred[base_test$var == lab[i]])}
conf <- data.frame(conf)
"Puis on calcule le taux de bien classés, l'accuracy vaut :"
print(mean(pred==base_test$var))