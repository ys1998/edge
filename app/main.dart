import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:convert';
import 'config.dart';
import 'session.dart';
import 'userclass.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'EDGE',
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: new LoginPage(),
    );
  }
}

// Login
class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final _formKey = GlobalKey<FormState>();
  final GlobalKey<ScaffoldState> _scaffoldKey = new GlobalKey<ScaffoldState>();
  String rollno;
  UserClass user = new UserClass();
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      key: _scaffoldKey,
      body: new SafeArea(
          top: false,
          bottom: false,
          child: new Form(
              key: _formKey,
              autovalidate: true,

              child: new ListView(
                shrinkWrap: true,
                padding: const EdgeInsets.all(50.0),
                children: <Widget>[
                  new Icon(
                    Icons.assessment,
                    size: 200.0,
                    color: Colors.lightBlue,
                  ),
                  const Text(
                    'EDGE',
                    style: const TextStyle(
                      color: Colors.blue,
                      fontWeight: FontWeight.w600,
                      fontSize: 36.0,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const Text(
                    'Efficient Digital Grading Environment\n',
                    textAlign: TextAlign.center,
                    style: TextStyle(color: Colors.black38),
                  ),
                  new TextFormField(
                    decoration: InputDecoration(
                      hintText: 'Username',
                      contentPadding: EdgeInsets.all(10.0),
                    ),
                    validator: (value) {
                      if (value.isEmpty) {
                        return 'Please enter username';
                      }
                    },
                    onSaved: (val) => user.name = val,
                  ),
                  new TextFormField(
                    obscureText: true,
                    decoration: InputDecoration(
                      hintText: 'Password',
                      contentPadding: EdgeInsets.all(10.0),
                    ),
                    validator: (value) {
                      if (value.isEmpty) {
                        return 'Please enter password';
                      }
                    },
                    onSaved: (val) => user.password = val,
                  ),
                  new Container(
                      padding: const EdgeInsets.all(10.0),
                      child: new RaisedButton(
                        color: Colors.blue,
                        child: const Text('LOGIN', style: const TextStyle(color: Colors.white)),
                        onPressed: (){
                          final FormState form = _formKey.currentState;
                          if(!form.validate()){
                            _scaffoldKey.currentState
                                .showSnackBar(new SnackBar(backgroundColor: Colors.red,
                                content: new Text(
                                    'Invalid details. Please review and correct.',
                                    textAlign: TextAlign.center
                                )));
                          }
                          else{
                            form.save();
                            Future<void> checkLogin() async {
                              String loginurl = Config.url +  'Login';
                              Session login = new Session();
                              var body = { 'uid': user.name , 'pwd': user.password };
                              var jsRet = await login.post(loginurl, body);
                              Map ret = json.decode(jsRet);
                              if(ret["status"] && ret["type"] == "student"){
                                Navigator.pushAndRemoveUntil(
                                    context,
                                    MaterialPageRoute(builder: (context) => HomePage(name : ret["name"], rollno : ret["rollno"])),
                                        (Route<dynamic> route) => false
                                );
                              }
                              else{
                                _scaffoldKey.currentState
                                    .showSnackBar(new SnackBar(
                                    backgroundColor: Colors.red,
                                    content: new Text(
                                            'Authentication failed.',
                                            textAlign: TextAlign.center
                                        )
                                    )
                                );
                              };
                            }
                            checkLogin();
                          }
                        },
                      )),
                  const Text(
                    '\n\u00a9 The EDGE Developers',
                    textAlign: TextAlign.center,
                    style: const TextStyle(
                        color: Colors.grey
                    ),
                  )
                ],
              ))
      ),
    );
  }
}

class HomePage extends StatefulWidget {
  HomePage({Key key, this.name, this.rollno}) : super(key: key);
  String rollno;
  String name;
  CoursePage current_page;
  @override
  _HomePage createState() => _HomePage();
}

class _HomePage extends State<HomePage>{
  @override
  void initState() {
    super.initState();
    widget.current_page = CoursePage(name: widget.name, rollno: widget.rollno, mode: 'student', refresh: false);
  }

  @override
  Widget build(BuildContext context){
    return Scaffold(
      backgroundColor: Color.fromARGB(255, 240, 240, 240),
      appBar: AppBar(
        title: Text('Courses'),
        actions: <Widget>[
          IconButton(
            icon: const Icon(Icons.refresh, color: Colors.white),
            onPressed: () {
              setState(() {
                widget.current_page = CoursePage(name: widget.name,
                    rollno: widget.rollno, refresh: true,
                    mode: widget.current_page.mode);
              });
            },
            tooltip: 'Reload',
          )
        ]),
      drawer: new Drawer(
        child: new Column(
          children: <Widget>[
            new UserAccountsDrawerHeader(
                accountName: Text(widget.name, style: TextStyle(fontSize: 20.0)),
                accountEmail: Text(widget.rollno, style: TextStyle(fontSize: 16.0),)
            ),
            new Column(
              children: <Widget>[
                Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text('SWITCH ROLE TO', style: TextStyle(color: Colors.black26)),
                ),
                ListTile(
                  title: Text('Student'),
                  leading: Icon(Icons.account_circle),
                  onTap: () {
                    setState(() {
                      Navigator.pop(context);
                      widget.current_page = CoursePage(name: widget.name,
                          rollno: widget.rollno, refresh: false,
                          mode: 'student');
                    });
                  },
                ),
                ListTile(
                  title: Text('Teaching Assistant'),
                  leading: Icon(Icons.contacts),
                  onTap: () {
                    setState(() {
                      Navigator.pop(context);
                      widget.current_page = CoursePage(name: widget.name,
                          rollno: widget.rollno, refresh: false,
                          mode: 'ta');
                    });
                  },
                ),
                Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text('STATISTICS', style: TextStyle(color: Colors.black26)),
                ),
                ListTile(
                  title: Text('Standing'),
                  leading: Icon(Icons.assessment),
                ),
                ListTile(
                  title: Text('Performance'),
                  leading: Icon(Icons.assignment_turned_in),
                ),
              ],
            ),
            Expanded(
              child: Align(
                alignment: FractionalOffset.bottomCenter,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: <Widget>[
                    FlatButton(
                      child: Text('CONTACT', style: TextStyle(color: Colors.blue)),
                    ),
                    FlatButton(
                      child: Text('LOGOUT', style: TextStyle(color: Colors.blue)),
                      onPressed: () async {
                        await Session().get(Config.url + 'Logout');
                        Navigator.pushAndRemoveUntil(
                            context,
                            new MaterialPageRoute(builder: (context) => LoginPage()),
                                (Route<dynamic> route) => false
                        );
                      },
                    )
                  ],
                )
              ),
            )
          ],
        ),
      ),
    body: widget.current_page
    );
  }
}

class CoursePage extends StatefulWidget {
  CoursePage({Key key, this.name, this.rollno, this.mode, this.refresh}) : super(key: key);
  String name, rollno, mode;
  bool refresh;
  @override
  _CoursePage createState() => _CoursePage();
}

class _CoursePage extends State<CoursePage>{
  bool loaded = false;
  List<List<String>> present_courses = null;
  List<List<String>> past_courses = null;

  Future<Map<String, List<List<String>>>> _fetchData() async {
    String course_url = Config.url +  'AllCourses' + '?rollno=' + widget.rollno + '&mode=' + widget.mode;
    Session fetchCourse = new Session();
    var res = await fetchCourse.get(course_url);
    final Map<String, dynamic> ret = json.decode(res);
    Map<String, List<List<String>>> result = new Map<String, List<List<String>>>();

    if(ret["status"]){
      result['present_courses'] = [];
      for(var x in ret['present_courses']){
        List<String> temp = new List<String>();
        for(var y in x){
          temp.add(y.toString());
        }
        result['present_courses'].add(temp);
      }
      if(widget.mode == 'student') {
        result['past_courses'] = [];
        for (var x in ret['past_courses']) {
          List<String> temp = new List<String>();
          for (var y in x) {
            temp.add(y.toString());
          }
          result['past_courses'].add(temp);
        }
      }
      return result;
    }else{
      return null;
    }
  }

  @override
  void initState() {
    super.initState();
    loaded = false;
    _fetchData().then((Map<String, List<List<String>>> value){
      setState(() {
        present_courses = value["present_courses"];
        past_courses = value["past_courses"];
        loaded = true;
        widget.refresh = false;
      });
    });
  }

  @override
  void didUpdateWidget(CoursePage oldWidget) {
    if (oldWidget.mode != widget.mode || widget.refresh) {
      loaded = false;
      _fetchData().then((Map<String, List<List<String>>> value){
        setState(() {
          present_courses = value["present_courses"];
          past_courses = value["past_courses"];
          loaded = true;
          widget.refresh = false;
        });
      });
    }
  }

  @override
  Widget build(BuildContext ctxt) {
      return (widget.mode == 'student') ?
      DefaultTabController(
      length: 2,
      child: Scaffold(
        backgroundColor: Color.fromARGB(255, 240, 240, 240),
        appBar: new AppBar(
          flexibleSpace: new Column(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                new TabBar(
                tabs: [
                    Tab(text: 'Current'),
                    Tab(text: 'Past'),
                  ]
                )
              ]
            )
          ),
        body: TabBarView(
          children:
            (loaded) ? [
            (present_courses.length > 0)?
            ListView.builder(
                padding: EdgeInsets.all(10.0),
                itemCount: present_courses.length,
                itemBuilder: (context, index){
                  return Card(
                    child: ListTile(
                      title: Text(present_courses.elementAt(index).elementAt(0)),
                      subtitle: Text(present_courses.elementAt(index).elementAt(1) +
                                      ' ' + present_courses.elementAt(index).elementAt(2)),
                      leading: Icon(Icons.book),
                      trailing: FlatButton(
                          onPressed: (){
                            Navigator.push(
                                context,
                                MaterialPageRoute(builder: (context) =>
                                    ExamPage(
                                        rollno: widget.rollno,
                                        course_id: present_courses.elementAt(index).elementAt(0),
                                        mode: widget.mode
                                    )
                                )
                            );
                          },
                          child: Text('VIEW EXAMS', style: TextStyle(color: Colors.blue))
                      ),
                    )
                  );
                }
            ) : Center(child: Text('No courses to display.', style: TextStyle(color: Colors.black38))),
          (past_courses.length > 0)?
          ListView.builder(
              padding: EdgeInsets.all(10.0),
              itemCount: past_courses.length,
              itemBuilder: (context, index){
                return Card(
                    child: ListTile(
                        title: Text(past_courses.elementAt(index).elementAt(0)),
                        subtitle: Text(past_courses.elementAt(index).elementAt(1) +
                            ' ' + past_courses.elementAt(index).elementAt(2)),
                        leading: Icon(Icons.book),
                        trailing: FlatButton(
                            onPressed: (){
                              Navigator.push(
                                  context,
                                  MaterialPageRoute(builder: (context) =>
                                      ExamPage(
                                          rollno: widget.rollno,
                                          course_id: past_courses.elementAt(index).elementAt(0),
                                          mode: widget.mode
                                      )
                                  )
                              );
                            },
                            child: Text('VIEW EXAMS', style: TextStyle(color: Colors.blue))
                        ),
                    )
                );
              }
          ) : Center(child: Text('No courses to display.', style: TextStyle(color: Colors.black38)))]
        : [
            Center(
              child: Text('Loading ...'),
            ),
            Center(
              child: Text('Loading ...'),
            )
          ],
        ),
      ),
    ) :
       (loaded) ?
       (present_courses.length > 0)?
          ListView.builder(
              padding: EdgeInsets.all(10.0),
            itemCount: present_courses.length,
            itemBuilder: (ctxt, index){
              return Card(
                child: ListTile(
                  title: Text(present_courses.elementAt(index).elementAt(0)),
                  subtitle: Text(present_courses.elementAt(index).elementAt(1) +
                                ' ' + present_courses.elementAt(index).elementAt(2)),
                  leading: Icon(Icons.book),
                  trailing: FlatButton(
                      onPressed: (){
                        Navigator.push(
                            context,
                            MaterialPageRoute(builder: (context) =>
                                ExamPage(
                                    rollno: widget.rollno,
                                    course_id: present_courses.elementAt(index).elementAt(0),
                                    mode: widget.mode
                                )
                            )
                        );
                      },
                      child: Text('VIEW EXAMS', style: TextStyle(color: Colors.blue))
                  ),
                )
              );
            }
          ):
          Center(
              child: Text('No courses to display.', style: TextStyle(color: Colors.black38))
          ):
       Center(
         child: Text('Loading ...'),
       );
  }
}

class ExamPage extends StatefulWidget {
  ExamPage({Key key, this.rollno, this.course_id, this.mode}) : super(key: key);
  String rollno, mode, course_id;
  @override
  _ExamPage createState() => _ExamPage();
}

class _ExamPage extends State<ExamPage>{
  bool loaded = false;
  List<List<String>> exam_details = null;

  Future<List<List<String>>> _fetchData() async {
    String exam_url = Config.url +  'AllExams' + '?rollno=' + widget.rollno + '&course_id=' + widget.course_id + '&mode=' + widget.mode;
    Session fetchExam = new Session();
    var res = await fetchExam.get(exam_url);
    final Map<String, dynamic> ret = json.decode(res);
    List<List<String>> result = new List<List<String>>();

    if(ret["status"]){
      for(var x in ret['data']){
        List<String> temp = new List<String>();
        temp.add(x['name']);
        temp.add(x['test_id']);
        temp.add(x['test_date']);
        result.add(temp);
      }
      return result;
    }else{
      return null;
    }
  }

  @override
  void initState() {
    super.initState();
    loaded = false;
    _fetchData().then((List<List<String>> value){
      setState(() {
        exam_details = value;
        loaded = true;
      });
    });
  }

  void refresh(){
    setState(() {
      loaded = false;
      _fetchData().then((List<List<String>> value){
        setState(() {
          exam_details = value;
          loaded = true;
        });
      });
    });
  }

  @override
  Widget build(BuildContext ctxt) {
    return Scaffold(
      backgroundColor: Color.fromARGB(255, 240, 240, 240),
      appBar: AppBar(
        title: Text('Exams'),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.refresh),
            onPressed: (){ refresh(); },
            tooltip: 'Reload',
          )
        ],
      ),
      body: (widget.mode == 'student') ?
        (loaded) ?
        (exam_details.length > 0)?
        ListView.builder(
            padding: EdgeInsets.all(10.0),
            itemCount: exam_details.length,
            itemBuilder: (context, index){
              return Card(
                  child: ListTile(
                    title: Text(exam_details.elementAt(index).elementAt(0) + ' (' +
                        exam_details.elementAt(index).elementAt(2) + ')'),
                    subtitle: Text('Test ID: ' + exam_details.elementAt(index).elementAt(1)),
                      leading: Icon(Icons.content_paste),
                      trailing: FlatButton(
                          onPressed: (){
                            Navigator.push(
                                context,
                                MaterialPageRoute(builder: (context) =>
                                    QuestionPage(
                                        title: exam_details.elementAt(index).elementAt(0),
                                        rollno: widget.rollno,
                                        course_id: widget.course_id,
                                        mode: widget.mode,
                                        test_id: exam_details.elementAt(index).elementAt(1)
                                    )
                                )
                            );
                          },
                          child: Text('QUESTIONS', style: TextStyle(color: Colors.blue))
                      ),
                  )
              );
            }
        ):
        Center(
            child: Text('No exams to display.', style: TextStyle(color: Colors.black38))
        ):
        Center(
          child: Text('Loading ...'),
        ) :
        (loaded) ?
        (exam_details.length > 0)?
        ListView.builder(
            padding: EdgeInsets.all(10.0),
            itemCount: exam_details.length,
            itemBuilder: (context, index){
              return Card(
                  child: ListTile(
                      title: Text(exam_details.elementAt(index).elementAt(0) + ' (' +
                          exam_details.elementAt(index).elementAt(2) + ')'),
                      subtitle: Text('Test ID: ' + exam_details.elementAt(index).elementAt(1)),
                      leading: Icon(Icons.content_paste),
                      trailing: FlatButton(
                          onPressed: (){
                            Navigator.push(
                                context,
                                MaterialPageRoute(builder: (context) =>
                                    QuestionPage(
                                        title: exam_details.elementAt(index).elementAt(0),
                                        rollno: widget.rollno,
                                        course_id: widget.course_id,
                                        mode: widget.mode,
                                        test_id: exam_details.elementAt(index).elementAt(1)
                                    )
                                )
                            );
                          },
                          child: Text('GRADE EXAM', style: TextStyle(color: Colors.blue))
                      ),
                  )
              );
            }
        ):
        Center(
            child: Text('You have not been assigned any exam.', style: TextStyle(color: Colors.black38))
        ):
        Center(
          child: Text('Loading ...'),
        )
    );
  }
}

class QuestionPage extends StatefulWidget {
  QuestionPage({Key key, this.title, this.rollno, this.course_id, this.mode, this.test_id}) : super(key: key);
  String title, rollno, mode, course_id, test_id;
  @override
  _QuestionPage createState() => _QuestionPage();
}

class _QuestionPage extends State<QuestionPage>{
  bool loaded = false;
  List<List<String>> question_details = null;

  Future<List<List<String>>> _fetchData() async {
    String ques_url = Config.url +  'ExamDetails' + '?rollno=' + widget.rollno + '&course_id='
        + widget.course_id + '&mode=' + widget.mode + '&test_id=' + widget.test_id;
    Session fetchQues = new Session();
    var res = await fetchQues.get(ques_url);
    final Map<String, dynamic> ret = json.decode(res);
    List<List<String>> result = new List<List<String>>();

    if(ret["status"]){
      for (var x in ret['data']) {
        List<String> temp = new List<String>();
        temp.add("Q" + x['index'].toString());
        if(x['marks_obt'] == null)
          temp.add("-- / " + x['m_marks']);
        else
          temp.add(x['marks_obt'] + ' / ' + x['m_marks']);
        if(x['grader'] == null)
          temp.add("unknown");
        else
          temp.add(x['grader']);
        result.add(temp);
      }
      return result;
    }else{
      return null;
    }
  }

  @override
  void initState() {
    super.initState();
    loaded = false;
    _fetchData().then((List<List<String>> value){
      setState(() {
        question_details = value;
        loaded = true;
      });
    });
  }

  void refresh(){
    setState(() {
      loaded = false;
      _fetchData().then((List<List<String>> value){
        setState(() {
          question_details = value;
          loaded = true;
        });
      });
    });
  }

  @override
  Widget build(BuildContext ctxt) {
    return Scaffold(
        backgroundColor: Color.fromARGB(255, 240, 240, 240),
        appBar: AppBar(
          title: Text(widget.title),
          actions: <Widget>[
            IconButton(
              icon: Icon(Icons.refresh),
              onPressed: (){ refresh(); },
              tooltip: 'Reload',
            )
          ],
        ),
        body: (widget.mode == 'student') ?
        (loaded) ?
        (question_details.length > 0)?
        ListView.builder(
            padding: EdgeInsets.all(10.0),
            itemCount: question_details.length,
            itemBuilder: (context, index){
              return Card(
                  child: ListTile(
                    title: Text('Score: ' + question_details.elementAt(index).elementAt(1)),
                    subtitle: Text('Grader: ' + question_details.elementAt(index).elementAt(2)),
                    leading: Text(question_details.elementAt(index).elementAt(0)),
                    trailing: FlatButton(
                        onPressed: (){},
                        child: Text('VIEW', style: TextStyle(color: Colors.blue))
                    ),
                  )
              );
            }
        ):
        Center(
            child: Text('No questions to display.', style: TextStyle(color: Colors.black38))
        ):
        Center(
          child: Text('Loading ...'),
        ) :
        (loaded) ?
        (question_details.length > 0)?
        ListView.builder(
            padding: EdgeInsets.all(10.0),
            itemCount: question_details.length,
            itemBuilder: (context, index){
              return Card(
                  child: ListTile(
                    title: Text(question_details.elementAt(index).elementAt(0) + ' ,' +
                        question_details.elementAt(index).elementAt(2)),
                    subtitle: Text('Test ID: ' + question_details.elementAt(index).elementAt(1)),
                    leading: Icon(Icons.content_paste),
                    trailing: FlatButton(
                        onPressed: (){},
                        child: Text('GRADE', style: TextStyle(color: Colors.blue))
                    ),
                  )
              );
            }
        ):
        Center(
            child: Text('You have not been assigned any exam.', style: TextStyle(color: Colors.black38))
        ):
        Center(
          child: Text('Loading ...'),
        )
    );
  }
}