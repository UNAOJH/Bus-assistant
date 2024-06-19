package BUS;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class State extends Application {

    private TextField userTextField;
    private PasswordField passwordField;
    private Button loginButton;
    private ComboBox<String> cityComboBox;
    private ComboBox<String> stationComboBox;
    private Label infoLabel;
    private File logFile;

    private ArrayList<String> getStations(String city) throws Exception {
        ArrayList<String> stations = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(city + ".txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            stations.add(parts[0]);
        }
        reader.close();
        return stations;
    }

    private ArrayList<String> getBuses(String city, String station) throws Exception {
        ArrayList<String> buses = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(city + ".txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String[] stations = station.split(",");
            if (parts[0].equals(stations[0])) {
                String[] busList = parts[1].split(";");
                for (String bus : busList) {
                    buses.add(bus);
                }
                break;
            }
        }
        reader.close();
        return buses;
    }

    private void writeToLog(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDatetime = sdf.format(new Date());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/log.txt", true));
            writer.write(currentDatetime + "\t " + message);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(Stage primaryStage) {
        Stage loginStage = new Stage();
        loginStage.setTitle("登录");

        VBox loginVBox = new VBox(20);
        loginVBox.setAlignment(Pos.CENTER);

        Label userLabel = new Label("用户名:");
        userLabel.setStyle("-fx-font-size: 16px;");
        userTextField = new TextField();
        userTextField.setPrefWidth(200);
        userTextField.setStyle("-fx-font-size: 16px;");

        Label passwordLabel = new Label("密码:");
        passwordLabel.setStyle("-fx-font-size: 16px;");
        passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setStyle("-fx-font-size: 16px;");

        loginButton = new Button("登录");
        loginButton.setStyle("-fx-background-color: #cf43f4;-fx-text-fill: #403aef; -fx-font-size: 16px; -fx-pref-width: 100px; -fx-padding: 10;");
        loginButton.setOnAction(event -> {
            String username = userTextField.getText();
            String password = passwordField.getText();
            boolean isUserValid = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("src/password.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String passWordFirst = parts[0];

//                    System.out.println(parts);
                    if (username.equals("admin") && password.equals(passWordFirst)) {
                        isUserValid = true;
                        break;
                    }
                }
                if (isUserValid) {
                    loginStage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "用户名或密码不正确！", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button setPasswordButton = new Button("设置密码");
        setPasswordButton.setOnAction(event -> {
            TextInputDialog oldPwdDialog = new TextInputDialog();
            oldPwdDialog.setTitle("旧密码验证");
            oldPwdDialog.setHeaderText("请输入当前密码：");
            oldPwdDialog.setContentText("密码：");

            Optional<String> oldPwdResult = oldPwdDialog.showAndWait();
            if (oldPwdResult.isPresent() && !oldPwdResult.get().isEmpty()) {
                String oldPassword = oldPwdResult.get();
                //读取保存的旧密码
                try (Scanner scanner = new Scanner(new FileInputStream("src/password.txt"))) {
                    String savedPasswords = scanner.nextLine();
                    String[] passwords = savedPasswords.split(",");
                    String correctPwd = passwords[passwords.length -1];
                    if (oldPassword.equals(correctPwd)) { //验证通过，继续设置新密码
                        TextInputDialog newPwdDialog = new TextInputDialog();
                        newPwdDialog.setTitle("设置新密码");
                        newPwdDialog.setHeaderText("请输入新密码：");
                        newPwdDialog.setContentText("密码：");

                        Optional<String> newPwdResult = newPwdDialog.showAndWait();
                        if (newPwdResult.isPresent() && !newPwdResult.get().isEmpty()) {
                            String newPassword = newPwdResult.get();
                            //清空密码文件，并写入新密码
                            try (FileWriter writer = new FileWriter("src/password.txt", false)) {
                                writer.write(newPassword);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "密码设置成功！", ButtonType.OK);
                                alert.showAndWait();
                            } catch (IOException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "密码保存失败！", ButtonType.OK);
                                alert.showAndWait();
                                return;
                            }
                        } else if (newPwdResult.isPresent() && newPwdResult.get().isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "密码不能为空！", ButtonType.OK);
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "密码设置已取消！", ButtonType.OK);
                            alert.showAndWait();
                        }
                    } else { //验证失败，提示用户
                        Alert alert = new Alert(Alert.AlertType.ERROR, "当前密码错误！", ButtonType.OK);
                        alert.showAndWait();
                    }
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "无法读取密码文件！", ButtonType.OK);
                    alert.showAndWait();
                } catch (NoSuchElementException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "密码文件已损坏！", ButtonType.OK);
                    alert.showAndWait();
                }
            } else if (oldPwdResult.isPresent() && oldPwdResult.get().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "密码不能为空！", ButtonType.OK);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "密码验证已取消！", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Button cancelButton = new Button("退出");
        cancelButton.setStyle("-fx-background-color: #d75a5a;-fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-pref-width: 100px; -fx-padding: 10;");
        cancelButton.setOnAction(event -> {
            loginStage.close();
            System.exit(0);
        });

        loginVBox.getChildren().addAll(userLabel, userTextField, passwordLabel, passwordField, loginButton, cancelButton, setPasswordButton);
        loginVBox.setStyle("-fx-background-color: #5de4c9;");
        Scene loginScene = new Scene(loginVBox, 450, 400);
        loginStage.setScene(loginScene);
        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.showAndWait();
    }


    /**
     * private void login(Stage primaryStage) {
     * Stage loginStage = new Stage();
     * loginStage.setTitle("登录");
     * <p>
     * VBox loginVBox = new VBox(20);
     * loginVBox.setAlignment(Pos.CENTER);
     * <p>
     * Label userLabel = new Label("用户名:");
     * userLabel.setStyle("-fx-font-size: 16px;");
     * userTextField = new TextField();
     * userTextField.setPrefWidth(200);
     * userTextField.setStyle("-fx-font-size: 16px;");
     * <p>
     * Label passwordLabel = new Label("密码:");
     * passwordLabel.setStyle("-fx-font-size: 16px;");
     * passwordField = new PasswordField();
     * passwordField.setPrefWidth(200);
     * passwordField.setStyle("-fx-font-size: 16px;");
     * <p>
     * // 修改开始
     * Button loginButton = new Button("登录");
     * loginButton.setStyle("-fx-background-color: #cf43f4;-fx-text-fill: #403aef; -fx-font-size: 16px; -fx-pref-width: 100px; -fx-padding: 10;");
     * loginButton.setOnAction(event -> verifyUser(userTextField.getText(), passwordField.getText(), loginStage));
     * // 修改结束
     * <p>
     * Button setPasswordButton = new Button("设置密码");
     * setPasswordButton.setOnAction(event -> {
     * // 略
     * });
     * <p>
     * // 添加一个退出按钮
     * Button cancelButton = new Button("退出");
     * cancelButton.setStyle("-fx-background-color: #d75a5a;-fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-pref-width: 100px; -fx-padding: 10;");
     * cancelButton.setOnAction(event -> {
     * System.exit(0);
     * });
     * <p>
     * loginVBox.getChildren().addAll(userLabel, userTextField, passwordLabel, passwordField, loginButton, cancelButton, setPasswordButton);
     * loginVBox.setStyle("-fx-background-color: #5de4c9;");
     * Scene loginScene = new Scene(loginVBox, 450, 400);
     * loginStage.setScene(loginScene);
     * loginStage.initStyle(StageStyle.UNDECORATED);
     * loginStage.showAndWait();
     * }
     */


    private class StationInfoWindow {
        private final Stage stage;
        private final Label stationNameLabel;
        private final Label busListLabel;

        public StationInfoWindow(String stationName, ArrayList<String> busList) {
            stage = new Stage();
            stage.setTitle(stationName);

            stationNameLabel = new Label(stationName);
            stationNameLabel.setStyle("-fx-font-size: 24px;-fx-text-fill: #003cff; -fx-padding: 20;");
            GridPane.setConstraints(stationNameLabel, 0, 0, 2, 1);

            busListLabel = new Label();
            busListLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ad89ef; -fx-padding: 20;");
            GridPane.setConstraints(busListLabel, 0, 1);

            updateBusList(busList);

            Button closeButton = new Button("关闭");
            closeButton.setStyle("-fx-background-color: #cf43f4;-fx-text-fill: #403aef; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-padding: 10;");
            closeButton.setOnAction(event -> stage.close());
            GridPane.setConstraints(closeButton, 0, 2);

            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(20);
            gridPane.setVgap(20);
            gridPane.setPadding(new Insets(20));
            gridPane.getChildren().addAll(stationNameLabel, busListLabel, closeButton);
            Scene scene = new Scene(gridPane, 600, 400);
            stage.setScene(scene);
        }

        private void updateBusList(ArrayList<String> busList) {
            String text = "";
            for (String bus : busList) {
                text += bus + "\n";
            }
            busListLabel.setText(text);
        }

        public void show() {
            stage.showAndWait();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        login(primaryStage);

        cityComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "东城区", "西城区", "鼎城区", "学校专线", "公司专线"
        ));
        cityComboBox.setPromptText("请选择城市");
        stationComboBox = new ComboBox<>();
        infoLabel = new Label();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String currentDatetime = sdf.format(new Date());
        logFile = new File("src/log" + ".txt");
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cityComboBox.setOnAction(event -> {
            String selectedCity = cityComboBox.getValue();
            try {
                ArrayList<String> stations = getStations(selectedCity);
                ObservableList<String> stationList = FXCollections.observableArrayList(stations);
                stationComboBox.setItems(stationList);

                writeToLog("用户选择城市：" + selectedCity);

            } catch (Exception e) {
                infoLabel.setText("获取站点列表失败");

                writeToLog("获取城市站点列表失败");
            }
        });




        Button searchButton = new Button("查询");
        searchButton.setStyle("-fx-background-color: #cf43f4;-fx-text-fill: #403aef; -fx-font-size: 24px; -fx-pref-width: 150px; -fx-padding: 10;");
        searchButton.setOnAction(event -> {
            String selectedCity = cityComboBox.getValue();
            String selectedStation = stationComboBox.getValue();
            if (selectedCity == null) {
                infoLabel.setText("请选择一个城市");
                return;
            }
            if (selectedStation == null) {
                infoLabel.setText("请选择一个站点");

                writeToLog("用户未选择站点");

                return;
            }

            try {
                ArrayList<String> buses = getBuses(selectedCity, selectedStation);
                String busText = "";
                for (String bus : buses) {
                    busText += bus + "  ";
                }
                infoLabel.setText("到达该站点的公交车有：" + busText);

                writeToLog("查询城市：" + selectedCity + "，站点：" + selectedStation + "，成功。\t\t用户端输出了{到达该站点的公交车有：" + busText + "}");

            } catch (Exception e) {
                infoLabel.setText("获取车辆信息失败");

                writeToLog("查询城市：" + selectedCity + "，站点：" + selectedStation + "，失败。");
            }
        });

        Button viewInfoButton = new Button("查看站点信息");
        viewInfoButton.setStyle("-fx-background-color: #ff9955; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px; -fx-padding: 5 10;");
        viewInfoButton.setOnAction(event -> {
            String selectedCity = cityComboBox.getValue();
            String selectedStation = stationComboBox.getValue();
            if (selectedStation == null) {
                infoLabel.setText("请选择一个站点");
                return;
            }
            try {
                ArrayList<String> buses = getBuses(selectedCity, selectedStation);
                StationInfoWindow stationInfoWindow = new StationInfoWindow(selectedStation, buses);
                stationInfoWindow.show();
                writeToLog("用户查看站点信息：" + selectedStation);

            } catch (Exception e) {
                infoLabel.setText("获取站点信息失败");
                writeToLog("获取站点信息失败");
            }
        });

        VBox vBox = new VBox(30);
        primaryStage.setTitle("常德公交小助手");
        Label titleLabel = new Label("常德公交小助手");
        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        vBox.setPadding(new Insets(40));
        vBox.getChildren().addAll(titleLabel);

        HBox cityHBox = new HBox(20);
        cityHBox.getChildren().addAll(new Label("城市:"), cityComboBox);
        HBox stationHBox = new HBox(20);
        stationHBox.getChildren().addAll(new Label("站点:"), stationComboBox);
        HBox searchHBox = new HBox(searchButton);
        searchHBox.setAlignment(Pos.CENTER_LEFT);
        HBox viewInfoHBox = new HBox(viewInfoButton);
        viewInfoHBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().addAll(cityHBox, stationHBox, searchHBox, viewInfoHBox, infoLabel);

        Button viewLogButton = new Button("查看日志");
        viewLogButton.setStyle("-fx-background-color: #808080; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 100px; -fx-padding: 5 10;");
        viewLogButton.setOnAction(event -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(logFile));
                String line;
                String logText = "";
                while ((line = reader.readLine()) != null) {
                    logText += line + "\n";
                }
                reader.close();

                Stage logStage = new Stage();
                logStage.setTitle("日志");

                ScrollPane scrollPane = new ScrollPane();
                Label logLabel = new Label(logText);
                logLabel.setPadding(new Insets(20));
                scrollPane.setContent(logLabel);

                Scene logScene = new Scene(scrollPane, 800, 400);
                logStage.setScene(logScene);
                logStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        HBox viewLogHBox = new HBox(viewLogButton);
        viewLogHBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().add(viewLogHBox);

        vBox.setStyle("-fx-background-color: #f4deb5;");
        cityHBox.setAlignment(Pos.CENTER_LEFT);
        stationHBox.setAlignment(Pos.CENTER_LEFT);
        searchHBox.setAlignment(Pos.CENTER_LEFT);
        viewInfoHBox.setAlignment(Pos.CENTER_RIGHT);

        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #003cff; -fx-padding: 20 0;");
        cityComboBox.setStyle("-fx-pref-width: 250px; -fx-font-size: 20px; -fx-padding: 10;");
        stationComboBox.setStyle("-fx-pref-width: 250px; -fx-font-size: 20px; -fx-padding: 10;");
        searchButton.setStyle("-fx-background-color: #cf43f4;-fx-text-fill: #403aef; -fx-font-size: 24px; -fx-pref-width: 150px; -fx-padding: 10;");
        viewInfoButton.setStyle("-fx-background-color: #ff9955; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px; -fx-padding: 5 10;");
        infoLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #ad89ef; -fx-padding: 10;");

        Scene scene = new Scene(vBox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}