printf "Calling : $1\n" 
for i in {1..7} #power
do
    printf "\n"
    echo "-----"
    echo "Power $i"
    echo "-----"
    printf "\n"

    for j in {1..10} #nombre de fois a rouler l'echantillon
    do
    ./client 52.88.144.244 $i $1
    done
    
done