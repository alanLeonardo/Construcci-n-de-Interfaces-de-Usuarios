import React, { Component } from 'react';
import { TouchableOpacity, Text, View, StyleSheet, ScrollView, ToastAndroid, Dimensions } from 'react-native';
import { SafeAreaView } from 'react-navigation';
import { itemWidth, slideHeight, colors } from '../../components/styles/utils';
import Line from '../../components/line';
import Api from '../../resources/Api';
import { AutoGrowTextInput } from 'react-native-auto-grow-textinput';
import Storage from '../../resources/Storage';
import Spinner from 'react-native-loading-spinner-overlay';

const styles = StyleSheet.create({
  column: {
    flex: 1,
    flexDirection: 'column'
  },
  row: {
    flex: 1,
    flexDirection: 'row'
  },
  cantReviews: {
    color: colors.white,
    textAlign: 'left',
    flex: 1
  },
  commentAndOverview: {
    color: colors.white,
    textAlign: 'left',
    marginLeft: 10,
    marginRight: 10,
    flex: 1
  },
  name: {
    fontSize: 16,
    color: colors.white,
    fontWeight: "bold",
    textAlign: 'center',
    backgroundColor: colors.black,
  },
  spinnerTextStyle: {
    color: '#FFF'
  },
  safeArea: {
    backgroundColor: colors.grey3,
  },
  container: {
    width: '100%',
    height: '100%',
    backgroundColor: colors.grey3,
  },
  container2: {
    paddingTop: 15,
    width: '100%',
  },
  buttonCommentText: {
    fontSize: 17,
    fontWeight: "bold",
    color: colors.white
  },
  buttonCommentContainer: {
    height: 30,
    padding: 2,
    justifyContent: 'center',
    alignItems: 'center',
    margin: 5,
    backgroundColor: colors.red,
    borderStyle: "solid",
    borderWidth: 2,
    borderRadius: 10,
    borderColor: colors.white
  },
  addCommentContainer: {
    flex: 1,
    padding: 20
  },
  textInput: {
    height: 40,
    paddingLeft: 6,
    backgroundColor: colors.white, borderColor: 'gray',
    borderWidth: 1,
    padding: 2,
  },
  commentContainer: {
    paddingBottom: 20,
    marginBottom: 5,
    flex: 1,
    borderColor: 'red',
    borderWidth: 1,
    width: Dimensions.get('window').width
  },
  errorText: {
    color: 'red'
  }
});

class CommentsScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      comments: [],
      loading: true,
      author: "",
      comment: "",
      error: ""
    };
  }

  componentDidMount() {
    const { id, comments } = this.props.navigation.state.params;
    this.setState({ comments: comments })
    Api.getMovie(id)
      .then(res => {
        this.updateReviews(res);
      })
      .catch(({ message }) => {
        this.setState({ error: message })
      });
  }

  static navigationOptions = {
    title: "Reviews",
    headerStyle: {
      backgroundColor: colors.pink2,
    },
    headerTintColor: colors.white,
    headerTitleStyle: {
      color: colors.white,
    },
  };

  // Realiza un comentario acerca de la pelicula.
  executeComment = () => {
    this.setState({ loading: true })
    Storage.getUser()
      .then(userToken => {
        this.setState({ author: userToken });
        this.commentMovie();
      })
      .catch(({ message }) => {
        this.setState({ error: message })
      });
  }

  // Envia una request con el comentario de la pelicula a la API. 
  commentMovie() {
    const { id } = this.props.navigation.state.params;
    const { author, comment } = this.state;
    Api.comment(id, { author, "content": comment })
      .then(() => {
        ToastAndroid.show("Commented successfully.", ToastAndroid.LONG);
        this.updateMovieWithComments(id);
      })
      .catch(({ message }) => {
        this.setState({ error: message })
      });
  }

  // Envia un request a la API, y actualiza los comentarios de la pelicula traida.
  updateMovieWithComments(id) {
    Api.getMovie(id)
      .then(res => {
        this.updateReviews(res)
        ToastAndroid.show("Update completed.", ToastAndroid.LONG);
      })
      .catch(({ message }) => {
        this.setState({ error: message })
      });
  }

  // Invierte el orden de comentarios, y los setea como estado.
  updateReviews(res) {
    const reviews = res.comments.reverse();
    this.setState({ comments: reviews, loading: false, comment: "" });
  }

  renderComment = (comments) => {
    return (
      <View>
        {comments.map((comment, index) => (
          <React.Fragment key={index}>
            <View style={styles.commentContainer}>
              <Text style={styles.name}>Author: {comment.author}</Text>
              <Text style={styles.commentAndOverview}>{comment.content}</Text>
            </View>
          </React.Fragment>
        ))}
      </View>
    )
  }

  comment() {
    return (
      <View style={{ width: '50%' }}>
        <TouchableOpacity onPress={() => {
          if (this.state.comment.trim() === "") {
            this.setState(() => ({ error: "Comment required." }));
          } else {
            this.setState(() => ({ error: null }));
            this.executeComment();
          }
        }}>
          <View style={styles.buttonCommentContainer}>
            <Text style={styles.buttonCommentText}>Comment</Text>
          </View>
        </TouchableOpacity>
      </View>
    )
  }

render() {
  const { loading, comment, comments } = this.state;
  return (
    <SafeAreaView style={styles.safeArea}>
      <View style={styles.container}>
        <ScrollView vertical={true}>
          <View style={styles.column}>

            <Spinner
              visible={loading}
              textContent={'Loading reviews...'}
              textStyle={styles.spinnerTextStyle}
            />

            <Line focus={false} />
            <View style={styles.addCommentContainer}>
              <AutoGrowTextInput
                placeholder="What do you think about that movie?..."
                value={comment}
                style={styles.textInput}
                onChangeText={comment => this.setState({ comment })}
              />
              {!!this.state.error && (
                <Text style={styles.errorText}>{this.state.error}</Text>
              )}
              {this.comment()}
            </View>

            <Text style={styles.cantReviews}>{`${comments.length} Reviews`}</Text>
            <View style={styles.row}>
              {this.renderComment(comments)}
            </View>

          </View>
        </ScrollView>
      </View>
    </SafeAreaView>
  );
}
}

export default CommentsScreen;