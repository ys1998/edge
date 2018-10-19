import 'package:flutter/material.dart';

void main() => runApp(new MyApp());

String _username;
String _password;

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      theme: new ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or press Run > Flutter Hot Reload in IntelliJ). Notice that the
        // counter didn't reset back to zero; the application is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: new LoginPage(),
    );
  }
}

class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final scaffoldKey = GlobalKey<ScaffoldState>();
  final formKey = GlobalKey<FormState>();


  void _submit() {
    final form = formKey.currentState;
    debugPrint("in submit form");
    if (form.validate()) {
      form.save();
      //*************************************
      // backend logic will come here LOGIC LOGIC
      //*************************************
      Navigator.of(context).push(
        new MaterialPageRoute<void>(   // Add 20 lines from here...
          builder: (BuildContext context) {
            return new CoursePage();
          },
        ),                           // ... to here.
      );
    }
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: scaffoldKey,
      appBar: AppBar(
        title: Text('Login to WhatAsap'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: formKey,
          child: Column(
            children: [
              TextFormField(
                decoration: InputDecoration(labelText: 'Username'),
                validator: (val) =>
                val.isEmpty ? 'Not a valid username' : null,
                onSaved: (val) => _username = val,
              ),
              TextFormField(
                decoration: InputDecoration(labelText: 'Password'),
                validator: (val) => null,
                onSaved: (val) => _password = val,
                obscureText: true,
              ),
              RaisedButton(
                onPressed: _submit,
                child: new Text('Login'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class CoursePage extends StatefulWidget {
  @override
  _CoursePage createState() => _CoursePage();
}

class _CoursePage extends State<CoursePage>{
  @override
  Widget build(BuildContext context){
    return MaterialApp(
      home: DefaultTabController(
        length: 2,
        child: Scaffold(
          appBar: AppBar(
            bottom: TabBar(
              tabs: [
                Tab(text: 'student'),
                Tab(text: 'TA'),
              ],
            ),
            title: Text('Tabs Demo'),
          ),
          body: TabBarView(
            children: [
              StudentPage(),
              TAPage(),
            ],
          ),
        ),
      ),
    );
  }
}

class StudentPage extends StatefulWidget {
  @override
  _StudentPage createState() => _StudentPage();
}

class _StudentPage extends State<StudentPage>{
  @override
  Widget build(BuildContext ctxt) {
    return Scaffold(
      appBar: AppBar(title: Text('Exams')),
      body: ListTile(
        title: Text('CS317'),
        onTap: () {
          // Update the state of the app
          // ... THIS IS JUST FOR TRIAL REAL LIST CONTRUCTION NEEDS SUPPORT FROM DATABASE
          // Then close the drawer
          Navigator.of(context).push(
              new MaterialPageRoute<void>(   // Add 20 lines from here...
                builder: (BuildContext context) {
                  return new ExamPage();
                },
              ),
          );
        },
      ),
      drawer: Drawer(
        // Add a ListView to the drawer. This ensures the user can scroll
        // through the options in the Drawer if there isn't enough vertical
        // space to fit everything.
        child: ListView(
          // Important: Remove any padding from the ListView.
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader(
              child: Text('Menu'),
              decoration: BoxDecoration(
                color: Colors.blue,
              ),
            ),
            ListTile(
              title: Text('Previous Courses'),
              onTap: () {
                // Update the state of the app
                // ... LOGIC  LOGIC
                // Then close the drawer
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: Text('Item 2'),
              onTap: () {
                // Update the state of the app
                // ...
                // Then close the drawer
                Navigator.pop(context);
              },
            ),
          ],
        ),
      ),
    );
  }
}

class ExamPage extends StatefulWidget {
  @override
  _ExamPage createState() => _ExamPage();
}

class _ExamPage extends State<ExamPage>{
  @override
  Widget build(BuildContext ctxt) {
    return Scaffold(
      body: Center(child: Text('Hi TA!! you too are a moron !!')),
    );
  }
}

////////////////////              TA INTERFACE       /////////////////////////////////////////

class TAPage extends StatefulWidget {
  @override
  _TAPage createState() => _TAPage();
}

class _TAPage extends State<TAPage>{
  @override
  Widget build(BuildContext ctxt) {
    return Scaffold(
      body: Center(child: Text('Hi TA!! you too are a moron !!')),
    );
  }
}