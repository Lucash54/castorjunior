library('org.renjin.cran:e1071')
"librairie e1071 chargée!"
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
long_app <- floor(total*0.7)
long_test <- total-long_app
"On crée la base d'apprentissage et la base de test (70/30)"
id_test=sample(seq_len(total), size = long_test)
base_test <- csv[id_test,]
"On démarre le svm"
sv <- svm(var~bref_size+cserver_size+vspace_bib, data=csv, subset=id_test)
"On affiche les valeurs prédites"
pred <- predict(sv,base_test)
"Puis on fait la matrice de confusion à la main"
"Programme à terminer! ;)"