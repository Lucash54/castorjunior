library('org.renjin.cran:randomForest')
"Librairie randomForest chargée!"
"Nombre d'arbres utilisés:"
nbArbre
"On regarde le nom des colonnes" 
col <- colnames(csv)
"La variable à expliquer est (s'il n'y a rien après cette ligne, la variable n'existe pas, il faudra checker le csv)"
colnames(csv)[which(colnames(csv)==nomVar)]
"On change le nom de la variable à expliquer en var"
colnames(csv)[which(colnames(csv)==nomVar)]="var"
"On transforme la variable à expliquer en facteur"
csv$var <- as.factor(as.character(csv$var))
"On lance l'arbre"
arbre <- randomForest(var~., data=csv, ntree=nbArbre)
"Puis on calcule l'accuracy"
taille <- length(levels(csv$var))
total <- length(csv$var)
conf <- arbre$confusion[,1:taille]
"On l'initialise la précision à 0"
accuracy <- 0
"La précision vaut la trace de la matrice de confusion divisée par le nombre d'individus classés"
for(i in 1:taille){accuracy <- accuracy + conf[i,i]/total}
"accuracy = "
accuracy
