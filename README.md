# Bus-assistant
用JavaFX编写的公交小助手

###前言
本程序是我在大一Java课程设计中开发的，旨在练习Java编程和JavaFX框架的使用。在编写程序的过程中，我参考了很多文献和教程，并借鉴了其他同学和开发者的经验和想法。在程序的设计和实现上，我尽力做到了合理、简洁和易于维护，但由于代码水平和经验有限，无法避免存在一些不足之处。

因此，如果您在使用本程序时发现了任何问题或有任何建议或意见，请不要犹豫，欢迎联系我指出。我非常乐意听取您的意见和建议，并会在后期的版本更新中尽可能地改进和完善程序。同时，也请您不要因为程序的不足而进行无意义的喷评或攻击，感谢您的理解和支持！

### 程序介绍

常德公交小助手是一个简单的查询常德公交车经过哪些站点的程序。用户可以选择城市和站点，然后查询到达该站点的公交车列表。本程序使用JavaFX框架设计界面，使用文件读写进行数据管理，支持用户登录和密码设置功能，同时记录用户行为和程序输出日志，方便后续维护和问题排查。

### 运行环境

- Java 8或以上版本

### 使用方法

1. clone或下载本项目源代码
2. 使用IDE打开项目，运行State.java文件
3. 登录后即可使用查询功能

###主要功能说明

(1)	用户登录界面；
(2)	选择城市界面；
(3)	站点选择界面；
(4)	查询输出界面；
(5)	站点信息界面；
(6)	日志信息界面；
(7)	日志输出到本地文件

### 功能列表

- 用户登录验证和密码设置功能
- 查询城市和站点列表
- 查询到达指定站点的公交车列表
- 查看站点信息功能
- 查看日志功能




/**程序函数与类名说明
- State类：主要用于程序启动、用户身份验证，实现了start()、main()、login()函数。
- TextField类：包含userTextField对象，用于在GUI界面中创建输入框。
- PasswordField类：包含passwordField对象，用于在GUI界面中创建密码输入框。
- Button类：包含loginButton、setPasswordButton和cancelButton对象，分别用于登录、设置密码和退出。
- ComboBox类：包含cityComboBox和stationComboBox对象，用于选择城市和站点。
- Label类：包含infoLabel和titleLabel对象，用于在GUI界面中显示相关信息。
- Util类：通过getStations()、getBuses()、writeToLog()函数实现数据读取和日志写入。
- ArrayList类：用于存储城市、站点和车辆信息。
- StationInfoWindow类：用于创建特定站点的信息展示窗口，通过构造函数和updateBusList()函数实现。
- Password类：实现verifyUser()函数用于验证用户的用户名和密码。
- File、BufferedReader、BufferedWriter：用于日志文件的读取和写入。
*/

### 注意事项

- 用户名为admin，密码为设置时设置的密码
- 城市和站点列表均存储在名为"{城市}.txt"的文件中，存储格式为"站点名称,到达此站点的公交车列表"，公交车列表用分号隔开
- 如果需要添加新城市和站点，请按照上述格式将数据添加到对应的文件中
- 日志文件存储在log.txt中，包括用户操作和程序输出日志
- 请勿随意更改、移动或删除程序所需要的文件

### 联系我们

如果您在使用程序时遇到任何问题或有任何建议或意见，请随时联系我们：

- email: 1265782380@qq.com
- wechat: MR_808800

### 版权声明

本程序仅供学习和研究使用，不得用于商业用途。如有侵权请及时联系我们。



