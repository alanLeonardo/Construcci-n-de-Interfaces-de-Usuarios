import React, { Component } from 'react';
import { Text, View, Image, TouchableOpacity, StyleSheet, ScrollView, Dimensions } from 'react-native';
import Title from '../../components/title';
import { SafeAreaView } from 'react-navigation';
import { colors } from '../../components/styles/utils';
import Line from '../../components/line';
import { withNavigation } from 'react-navigation';
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
  description: {
    color: colors.white,
    textAlign: 'center',
    flex: 1
  },
  descriptionGenre: {
    color: colors.white,
    textAlign: 'center',
    padding: 2,
    borderColor: colors.grey,
    borderWidth: 1,
    marginLeft: 3,
    marginRight: 3,
    marginBottom: 3,
    marginTop: 3,
  },
  genreView: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    alignItems: 'center',
  },
  buttonsView: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 5
  },
  commentAndOverview: {
    color: colors.white,
    textAlign: 'left',
    marginLeft: 10,
    marginRight: 10,
    flex: 1
  },
  cover: {
    // width: 395,
    width: Dimensions.get('window').width,
    height: 200
  },
  name: {
    fontSize: 16,
    color: colors.white,
    fontWeight: "bold",
    textAlign: 'center',
    backgroundColor: colors.black,
  },
  safeArea: {
    backgroundColor: colors.grey3,
  },
  container: {
    width: '100%',
    height: '100%',
    backgroundColor: colors.grey3,
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
  author: {
    fontSize: 16,
    color: colors.white,
    fontWeight: "bold",
    textAlign: 'center',
    backgroundColor: colors.black,
  },
  reviewsHeader: {
    padding: 2,
    fontSize: 22,
    color: colors.white,
    fontWeight: "bold",
    backgroundColor: colors.black,
  }
});

class DetailsScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true
    };
  }

  componentDidMount() {
    this.setState({ loading: false });
  }

  static navigationOptions = {
    title: "Details",
    headerStyle: {
      backgroundColor: colors.pink2,
    },
    headerTintColor: colors.white,
    headerTitleStyle: {
      color: colors.white,
    },
  };

  goToComments = () => {
    this.setState({ loading: true });
    this.props.navigation.push('Comments', this.props.navigation.state.params);
    this.setState({ loading: false });
  }

  buttonViewAllComments() {
    return (
      <View style={{ width: '50%' }}>
        <TouchableOpacity onPress={this.goToComments}>
          <View style={styles.buttonCommentContainer}>
            <Text style={styles.buttonCommentText}>More Reviews</Text>
          </View>
        </TouchableOpacity>
      </View>
    )
  }

  renderContent() {
    const { title, voteCount, img, genre, voteAverage, releaseDate, comments, popularity, overview } = this.props.navigation.state.params;
    const reversedComments = comments.reverse();

    return (
      <SafeAreaView style={styles.safeArea}>
        <View style={styles.container}>

          <Spinner
            visible={this.state.loading}
            textContent={'Loading details...'}
            textStyle={styles.spinnerTextStyle}
          />

          <ScrollView vertical={true}>
            <View style={styles.column}>
              <Image style={styles.cover} source={{ uri: img }} />
              <Title text={title} />
              <Line focus={false} />
              <View style={styles.row}>

                <View style={styles.column}>
                  <Text style={styles.name}>Release Date:</Text>
                  <Text style={styles.description}>{releaseDate}</Text>

                  <Text style={styles.name}>Genres:</Text>
                  <View style={styles.genreView}>{genre.map((genre, index) => <Text key={index} style={styles.descriptionGenre}>{genre}</Text>)}</View>

                  <Text style={styles.name}>Average:</Text>
                  <Text style={styles.description}>{`⭐ ${voteAverage}`}</Text>

                  <Text style={styles.name}>Popularity:</Text>
                  <Text style={styles.description}>{popularity}</Text>

                  <Text style={styles.name}>Vote Count:</Text>
                  <Text style={styles.description}>{voteCount}</Text>

                  <Text style={styles.name}>Overview:</Text>
                  <Text style={styles.commentAndOverview}>{overview}</Text>

                  <Text style={styles.reviewsHeader}>Reviews:</Text>
                  <Line focus={false} />
                  <Text style={styles.author}>Author: {reversedComments[0].author}</Text>
                  <Text style={styles.commentAndOverview}>{reversedComments[0].content}</Text>
                  <Line focus={false} />

                  <View style={styles.buttonsView}>
                    {this.buttonViewAllComments()}
                  </View>
                </View>
              </View>
            </View>
          </ScrollView>
        </View>
      </SafeAreaView>
    );
  }

  render() {
    return (
      <SafeAreaView >
        <View >
          {this.renderContent()}
        </View>
      </SafeAreaView>
    );
  }
}

export default withNavigation(DetailsScreen)