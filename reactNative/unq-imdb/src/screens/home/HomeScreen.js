import React from 'react';
import { View, StyleSheet, ToastAndroid } from 'react-native';
import { FlatList } from 'react-native-gesture-handler';
import { SafeAreaView } from 'react-navigation';

import Api from '../../resources/Api';

import Spinner from 'react-native-loading-spinner-overlay';
import CarouselMovies from '../../components/CarouselMovies';
import SearchBar from '../../components/searchBar';
import Logout from '../../components/logout';
import { colors } from '../../components/styles/utils';

class HomeScreen extends React.Component {
  _isMounted = false;

  constructor(props) {
    super(props);
    this.state = {
      data: [],
      loading: true,
      search: '',
      nextPage: 1
    };
  }

  static navigationOptions = {
    title: "Unq-Imdb",
    headerRight: () => <Logout />,
    headerStyle: {
      backgroundColor: colors.pink2,
    },
    headerTintColor: colors.white,
    headerTitleStyle: {
      color: colors.white,
    },
  };

  componentDidMount() {
    this._isMounted = true;
    Api.getTop()
      .then(data => this.setState({ data, loading: false }));
  }

  componentWillUnmount() {
    this._isMounted = false;
  }

  onSearch = () => {
    const text = this.state.search;
    this.setState({ search: '' }, () => this.props.navigation.push('Search', { text }));
  }

  handleLoadMore = () => {
    if (this.state.loading === false) {
      this.setState({ loading: true })
      Api.getMoreMoviesByGenre(this.state.nextPage)
        .then((res) => {
          this.setState({
            data: [...this.state.data, ...res],
            nextPage: this.state.nextPage + 1,
            loading: false
          })
        })
        .catch((err) => {
          ToastAndroid.show("There's no more categories.", ToastAndroid.LONG)
          this.setState({ loading: false })
        })
    }
  }

  renderContent() {
    const { loading, data, search } = this.state;
    return (
      <React.Fragment>
        <Spinner
          visible={loading}
          textContent={'Loading movies by category...'}
          textStyle={styles.spinnerTextStyle}
        />
        <SearchBar onChange={search => this.setState({ search })} value={search} onSubmit={this.onSearch} />
        <FlatList
          data={data}
          renderItem={CarouselMovies}
          keyExtractor={item => item.genre}
          showsVerticalScrollIndicator={false}
          onEndReachedThreshold={0.5}
          onEndReached={this.handleLoadMore}
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
  }
});


export default HomeScreen;
