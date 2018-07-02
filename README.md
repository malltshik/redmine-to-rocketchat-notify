## redmine-to-rocketchat-notify
#### What is application doing:
- Sync redmine active users to embedded filed database (H2)
- Notify users, who did not spend enough time in redmine for current day
(hours limit could be changed from 0 to 8 hours through web-ui)
- Search employees through web-ui - double `S` shortcut

#### Required properties
Before run application you have to declare couple of properties. 

- `database.path` - path to database file (will be created if does not exist)
- `redmine.address` - redmine server http link `http://redmine.net`
- `redmine.token` - redmine API token [(more there)](http://www.redmine.org/projects/redmine/wiki/Rest_api#Authentication)
- `rocketchat.address` - rocketchat server http link `http://rocketchat.net`
- `rocketchat.username` - who will send a messages
- `rocketchat.password` - user account password 


#### How to launch
- build project (have to be in project dir)
```bash
./gradlew build
```

- run application via `java -jar`
Do not forget! You have to declare properties. Set them to `application.properties`
file in the same directory as `.jar` file. 

```bash
vim application.properties
cp backend/build/libs/backend-${version}-SNAPSHOT.jar ./
java -jar backend-${version}-SNAPSHOT.jar
```

