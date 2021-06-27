import React, { Component } from 'react';
import { Text, View, Image, TouchableOpacity, StyleSheet, ScrollView } from 'react-native';
import { IS_IOS, itemWidth, slideHeight, colors } from './styles/utils';
import Api from '../resources/Api';
import Storage from '../resources/Storage';
import { withNavigation } from 'react-navigation';

const styles = StyleSheet.create({
  slideInnerContainer: {
    width: itemWidth,
    height: slideHeight,
  },
  imageContainer: {
    flex: 1,
    marginBottom: IS_IOS ? 0 : -1, // Prevent a random Android rendering issue
    backgroundColor: 'transparent',
  },
  image: {
    ...StyleSheet.absoluteFillObject,
    resizeMode: 'center',
  },
  textContainer: {
    position: 'absolute',
    bottom: IS_IOS ? 20 : 12,
    width: itemWidth,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 10,
    backgroundColor: colors.black,
  },
  title: {
    color: colors.white,
    fontSize: 13,
    fontWeight: 'bold',
    letterSpacing: 0.5
  },
  buttonContainer: {
    width: 50,
  },
  myInput: {
    backgroundColor: 'rgb(232, 232, 232)',
    borderRadius: 30,
    height: 40,
    width: '90%'},

  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#8ee3ab',
  },
});

class Movie extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showTitle: false,
      isAlertCommentVisible: false,
      dialogMovieVisible: false,
      dialogDataMovieVisible: false,
      dialogOverview: false,
      dialogComments: false,
      title: "",
      id: 0,
      voteCount: 0,
      genre: [""],
      voteAverage: 0, 
      comments: [], 
      releaseDate: "",
      popularity: 0, 
      overview: "",
      author: ""
    }
  }

  executeComment = (content) => {
    const { data: { id } } = this.props;

    Storage.getUser()
    .then(userToken => {
      this.setState({author: userToken});
      const author = this.state.author;

      Api.comment(id,{ author, content })
      .then(() => this.setState({isAlertCommentVisible:false}))
      .catch(({ message }) => this.setState({ error: message }))
    })
    .catch(({ message }) => this.setState({ error: message }));
  }

  showTitle = () => this.setState({ showTitle: true })

  renderImg = (withoutTouch) => {
    const { data: { title, img } } = this.props;
    const uppercaseTitle = <Text style={styles.title}>{title.toUpperCase()}</Text>;
    const content = (
      <React.Fragment>
        <View style={styles.imageContainer}>
          <Image source={{ uri: img }} style={styles.image} />
        </View>
        {this.state.showTitle && <View style={styles.textContainer}>{uppercaseTitle}</View>}
      </React.Fragment>
    )
    return (withoutTouch ? content :
      <TouchableOpacity activeOpacity={1} style={styles.slideInnerContainer} onPress={!this.state.showTitle && this.showTitle}>
        {content}
      </TouchableOpacity>
    );
  }

  showDialog = () => {
    this.setState({ dialogMovieVisible: true });
  };

  showOverview = () => {
    this.setState({ dialogOverview: true });
  };

  showComments = () => {
    this.setState({ dialogComments: true });
  };

  showDataDialog = () => {
    const { data: { title, id, voteCount, genre, voteAverage, releaseDate, popularity, overview } } = this.props;
    this.setState({title:title, id:id, voteCount:voteCount, voteAverage:voteAverage, genre:genre, releaseDate:releaseDate, popularity:popularity, overview:overview}); 

    Api.getMovie(id)
          .then(res => this.setState({comments: res.comments }))
          .catch(({ message }) => this.setState({ error: message }));

    this.setState({ dialogDataMovieVisible: true });
    this.setState({ dialogMovieVisible: false });
  };

  showCommentDialog = () => {
    this.setState({isAlertCommentVisible:true});
    this.setState({ dialogMovieVisible: false });
  };
 
  handleCancel = () => {
    this.setState({ dialogMovieVisible: false });
  };

  handleDataCancel = () => {
    this.setState({ dialogDataMovieVisible: false });
  };

  handleOverviewClose = () => {
    this.setState({ dialogOverview: false });
  };
  
  handleCommentsClose = () => {
    this.setState({ dialogComments: false });
  };
 
  handleDelete = () => {
    this.setState({ dialogMovieVisible: false });
  };

  render () {
    return this.state.showTitle ? (
      <TouchableOpacity activeOpacity={1} style={styles.slideInnerContainer} onPress={() => this.props.navigation.push('Details', this.props.data)}>
         {this.renderImg(true)}
      </TouchableOpacity>
          // <Dialog.Container visible={this.state.dialogMovieVisible}>
          //   <Dialog.Title>Movie</Dialog.Title>
          //   <Dialog.Button label="Cancel" onPress={this.handleCancel} />
          //   <Dialog.Button label="View Data" onPress={this.showDataDialog} />
          //   <Dialog.Button label="Comment" onPress={this.showCommentDialog} />
          // </Dialog.Container>        

          // <DialogInput isDialogVisible={this.state.isAlertCommentVisible}
          //             title={"Comment Movie"}
          //             message={"Enter a comment"}
          //             hintInput ={"hint for the input"}
          //             submitInput={ (inputText) => {this.executeComment(inputText)} }
          //             closeDialog={ () =>this.setState({isAlertCommentVisible:false})}>
          // </DialogInput>
    ):
    this.renderImg();
  }
}

export default withNavigation(Movie)