# LiveQuestions

README also available in english!
https://github.com/vitor-ramos/livequestions/blob/main/README.en.md

### Sobre
Live questions é um aplicativo para consulta de perguntas de qualquer site da Stack Exchange. As perguntas são mostradas de acordo com o site selecionado e é possível filtrá-las por tag. Tanto na tela de seleção do site quanto na tela de seleção da tag há uma barra para pesquisa.

### Implementação
A UI está sendo implementada usando Jetpack Compose (ainda em alpha), as requests são feitas usando Retrofit e o cache da lista de sites utiliza Room. A API do shared preferences é usada para guardar a tag selecionada e o site selecionado.

### Funcionalidades planejadas
* Tela da pergunta, com corpo, anexos e respostas;
* interações que requerem login (responder, votar, comentar, etc);
* perguntar;
* notificações

### Telas

Tela de perguntas | Tela de tags | Tela de sites
--- | --- | ---
![Tela de perguntas](https://github.com/vitor-ramos/livequestions/blob/main/readme/questions.png?raw=true) | ![Tela de tags](https://github.com/vitor-ramos/livequestions/blob/main/readme/tags.png?raw=true) | ![Tela de sites](https://github.com/vitor-ramos/livequestions/blob/main/readme/sites.png?raw=true)
