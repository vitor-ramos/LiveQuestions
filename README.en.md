# LiveQuestions

README também disponível em português!
https://github.com/vitor-ramos/livequestions/blob/main/README.md

### About
Live questions lists questions from any Stack Exchange site. The list is shown accordingly to the selected site and can be filtered by tag. Both the sites and the tags screen have a search bar.

### Implementation
The UI is being implemented using Jetpack Compose (still alpha), requests are made using Retrofit and sites list cache uses Room. The selected tag and site are stored using shared preferences.

### Planned features
* Question screen, with body, attachments and answers;
* interactions requiring auth, such as answering, voting, commenting;
* asking questions;
* notifications.

### Screenshots

Questions screen | Tags screen | Sites screen
--- | --- | ---
![Questions screen](https://github.com/vitor-ramos/livequestions/blob/main/readme/questions_en.png?raw=true) | ![Tags screen](https://github.com/vitor-ramos/livequestions/blob/main/readme/tags_en.png?raw=true) | ![Sites screen](https://github.com/vitor-ramos/livequestions/blob/main/readme/sites_en.png?raw=true)
