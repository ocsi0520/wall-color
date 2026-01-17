if ! [ -f ".env" ] ; then
    tail env-example.txt -n +4 > .env
    echo ".env is created from env-example.txt"
else
    echo ".env exists already"    
fi
