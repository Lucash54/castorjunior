library('org.renjin.cran:rpart')
"Librairie rpart chargée!"
"On regarde le nom des colonnes" 
col <- colnames(csv)
"La variable à expliquer est (s'il n'y a rien après cette ligne, la variable n'existe pas, il faudra checker le csv)"
colnames(csv)[which(colnames(csv)==nomVar)]
"On simplifie le nom de la variable à expliquer"
colnames(csv)[which(colnames(csv)==nomVar)]="var"
"On transforme la variable à expliquer en facteur"
csv$var <- as.factor(as.character(csv$var))
"Nombre de lignes du csv: "
total <- length(csv$var)
"On crée la base d'apprentissage et la base de test (70/30)"
long_app <- floor(total*propApp)
id_app=sample(seq_len(total), size = long_app)
base_test <- csv[-id_app,]
"On lance l'arbre CART"
arbre <- rpart(var~., data=csv, subset=id_app)
"Puis on predit les classes de la variable"
pred <- predict(arbre,base_test)
ind <- c()
classe <- c()
for(i in 1:nrow(pred)){ind[i] = which.max(pred[i,]); classe[i]=colnames(pred)[ind[i]]}
"Et on calcule l'accuracy, qui vaut : "
print(mean(classe==base_test$var))