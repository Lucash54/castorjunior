library('org.renjin.cran:randomForest')
"Librairie randomForest chargée!"
"Nombre d'arbres utilisés:"
nbArbre
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
long_app <- floor(total*propApp)
"Nombre de lignes du test =  arrondi((1-propApp)*total)"
long_test <- total-long_app
"On crée la base d'apprentissage et la base de test (70/30)"
id_test=sample(seq_len(total), size = long_test)
"On lance l'arbre"
arbre <- randomForest(var~., data=csv, subset=id_test,ntree=nbArbre)
"Puis on calcule l'accuracy"
taille <- length(levels(csv$var))
"On définit la matrice de confusion"
conf <- arbre$confusion[,1:taille]
"On l'initialise la précision à 0"
accuracy <- 0
"La précision vaut la trace de la matrice de confusion divisée par le nombre d'individus classés, i-e la longueur de l'échantillon de test"
for(i in 1:taille){accuracy <- accuracy + conf[i,i]/long_test}
"accuracy = "
print(accuracy)