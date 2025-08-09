# Add Tools Support to Gemma3:

## Create your version of gemma3:12b
```shell
ollama create Mradul/gemma3-tools:12b -f ./gemma3/12b/Modelfile
```

## Create your version of gemma3:27b
```shell
ollama create Mradul/gemma3-tools:27b -f ./gemma3/27b/Modelfile
```

## Pull Model
```shell
ollama pull Mradul/gemma3-tools:12b
ollama pull Mradul/gemma3-tools:27b
```