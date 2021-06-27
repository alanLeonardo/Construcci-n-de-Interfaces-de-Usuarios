import React from 'react';
import { View, StyleSheet } from 'react-native';
import { FlatList } from 'react-native-gesture-handler';
import { SafeAreaView } from 'react-navigation';

import Api from '../../resources/Api';
import Loading from '../../components/loading';

import Movie from '../../components/movie';
import EmptyList from '../../components/emptyList';
import Logout from '../../components/logout';
import { colors } from '../../components/styles/utils';

class SeeMoreScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      search: '',
      genre: this.props.navigation.state.params.genre
    };
  }

  static navigationOptions = ({ navigation }) => {
    const genre = navigation.state.params.genre;
    const gesturesEnabled =
      navigation &&
      navigation.state &&
      navigation.state.params &&
      navigation.state.params.genre === 'StackWithEnabledGestures';
    return {
      title: `${JSON.stringify(genre)}`,
      gesturesEnabled,
      headerRight: () => <Logout />,
      headerStyle: {
        backgroundColor: colors.pink2,
      },
      headerTintColor: colors.white,
      headerTitleStyle: {
        color: colors.white,
      },
    };
  };


  componentDidMount() {
    Api.getTopByGenre(this.state.genre)
      .then(data => {
        this.setState({ data, loading: false })
      });
  }

  onSearch = () => {
    const text = this.state.search;
    this.setState({ search: '' }, () => this.props.navigation.push('Search', { text }));
  }

  renderMovie = ({ item }) => <Movie id={`movie_${item.id}`} data={item} />

  renderContent() {
    const { loading, data } = this.state;
    if (loading) {
      return <Loading />;
    }
    return (
      <React.Fragment>
        <FlatList
          contentContainerStyle={{ alignSelf: 'center' }}
          data={data}
          renderItem={this.renderMovie}
          keyExtractor={item => `movie_${item.id}`}
          ListEmptyComponent={() => <EmptyList text={this.state.genre} />}
        />
      </React.Fragment>
    );
  }

  render() {
    return (
      <SafeAreaView style={styles.safeArea}>
        <View style={styles.container}>
          {this.renderContent()}
        </View>
      </SafeAreaView>
    );
  }
}

const styles = StyleSheet.create({
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
  }
});

export default SeeMoreScreen;