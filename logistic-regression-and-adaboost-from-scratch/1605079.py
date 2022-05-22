# %%
import pprint as pp
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn import preprocessing
from sklearn.model_selection import train_test_split
import argparse
from sklearn.preprocessing import MinMaxScaler, StandardScaler, KBinsDiscretizer
from sklearn.metrics import accuracy_score

np.random.seed(111)


class InformationGain:

    def __init__(self, df, num_cols, label):
        self.df = df
        self.num_cols = num_cols
        self.label = label
        self.original_columns = self.df.columns

    def get_final_column_list(self, num_of_features):
        self.cont_to_bins_pipeline()  # careful here!
        gain_dict = {col: self.calculate_gain(col) for col in list(set(self.df.columns) - {self.label})}
        sorted_gain_dict = {k: v for k, v in sorted(gain_dict.items(), key=lambda item: item[1])}
        # print('Sorted gain dict:', sorted_gain_dict)
        cols_to_drop = list(sorted_gain_dict.keys())[:len(sorted_gain_dict) - num_of_features]
        final_cols = list(set(self.original_columns) - set(cols_to_drop))
        # print('Final Cols are: ==>')
        # print(final_cols)

        return final_cols

    def calculate_gain(self, attribute):
        if self.label != attribute:
            p = len(self.df[self.df[self.label] == 1])
            df_size = len(self.df)  # p + n
            data_entropy = self.calculate_entropy(p / df_size)
            # print('Data entropy is', data_entropy)

            attribute_remainder = self.calculate_remainder(attribute)
            return data_entropy - attribute_remainder

    def calculate_remainder(self, attribute):
        # print('testing ==> ', attribute)
        # print(self.df[attribute].head())
        unique_vals = self.df[attribute].unique().tolist()

        remainder_sum = 0
        for index, attrib_val in enumerate(unique_vals):

            # choosing the rows equal to the unique value in the attribute
            filtered_df = self.df.where(self.df[attribute] == attrib_val).dropna()

            # print(f'{attrib_val} - {len(filtered_df)}')

            # calculating number of positive classed rows for the given attribute's unique value
            pk = len(filtered_df[filtered_df[self.label] == 1])
            nk = len(filtered_df[filtered_df[self.label] == 0])

            # sanity check
            assert nk == len(filtered_df) - pk
            # print(f'{attribute} - {attrib_val} (pk+nk): {pk+nk}')

            prob = (pk + nk) / (len(self.df))
            if prob != 0:
                attr_entropy = self.calculate_entropy(pk / (pk + nk))
                remainder_sum += prob * attr_entropy

        return remainder_sum

    @staticmethod
    def calculate_entropy(q):
        if q > 0:
            return -1 * (q * np.log2(q) + (1 - q) * np.log2(1 - q))
        return 0

    def convert_cont_to_bins(self, old_col_name):
        min = self.df[old_col_name].min()
        median = self.df[old_col_name].median()
        max = self.df[old_col_name].max()
        epsilon = 1e-3

        if len(self.df[old_col_name].unique()) > 2:
            # print('Binning:', old_col_name)
            self.df[old_col_name + '_'] = pd.cut(self.df[old_col_name], bins=[min - epsilon, median, max + epsilon],
                                                 labels=['b1', 'b2'], duplicates='drop')
            self.df.drop(columns=[old_col_name], inplace=True)
            self.df.rename(columns={old_col_name + '_': old_col_name}, inplace=True)

    def cont_to_bins_pipeline(self):
        for col in list(set(self.num_cols) - set(self.label)):
            self.convert_cont_to_bins(col)


class Utility:

    @staticmethod
    def get_whitespace_count(df):
        columns = df.columns
        dict = {}
        for col in columns:
            dict[col] = df[col].str.isspace().sum() if df[col].dtype == 'object' else -1

        pp.pprint(dict)

    @staticmethod
    def get_value_counts(df, num_cols):
        for col in num_cols:
            print(df[col].value_counts())

    @staticmethod
    def transformStandardScaler(df, transformable_columns, label):
        test = df.copy()

        if label in transformable_columns:
            transformable_columns.remove(label)

        test[transformable_columns] = StandardScaler().fit_transform(test[transformable_columns])

        return test

    @staticmethod
    def transformMinMaxScaler(df, transformable_columns, label):
        test = df.copy()

        if label in transformable_columns:
            transformable_columns.remove(label)

        test[transformable_columns] = MinMaxScaler().fit_transform(test[transformable_columns])

        return test

    @staticmethod
    def transformKBinsDiscretizer(df, transformable_columns, label, bins):

        if label in transformable_columns:
            transformable_columns.remove(label)

        for col in transformable_columns:
            est = KBinsDiscretizer(n_bins=bins, encode='ordinal', strategy='uniform')
            df[col] = est.fit_transform(df[[col]])

    @staticmethod
    def get_binary_col_count(df, columns):
        return [col for col in columns if len(df[col].value_counts()) == 2]

    def get_all_cols(self, df):
        columns = list(df.columns)
        columns_with_nan = df.columns[df.isna().any()].tolist()
        num_cols = list(df._get_numeric_data().columns)
        cat_cols = list(set(columns) - set(num_cols))
        cat_cols_with_nan = set(columns_with_nan) - set(num_cols)
        num_cols_with_nan = set(columns_with_nan) - set(cat_cols)
        binary_cols = self.get_binary_col_count(df, columns)

        return {'columns': columns,
                'columns_with_nan': columns_with_nan,
                'num_cols': num_cols,
                'cat_cols': cat_cols,
                'cat_cols_with_nan': cat_cols_with_nan,
                'num_cols_with_nan': num_cols_with_nan,
                'binary_cols': binary_cols}


class MetricCalculator:
    def __init__(self, y_real, y_pred) -> None:
        self.TP = 0
        self.TN = 0
        self.FP = 0
        self.FN = 0
        self.y_real = y_real
        self.y_pred = y_pred
        self.num_rows = len(y_pred)

    def calculate_cf_matrix_fields(self):

        self.y_real[self.y_real == 0] = -1

        for index in range(self.num_rows):
            if self.y_real[index] == 1 and self.y_pred[index] == 1:
                self.TP += 1
            if self.y_pred[index] == 1 and self.y_real[index] == -1:
                self.FP += 1
            if self.y_real[index] == -1 and self.y_pred[index] == -1:
                self.TN += 1
            if self.y_pred[index] == -1 and self.y_real[index] == 1:
                self.FN += 1

    def get_cf_field(self):
        return self.TP, self.TN, self.FP, self.FN

    def calculate_all_metric(self):
        self.calculate_cf_matrix_fields()
<<<<<<< HEAD

        print(f'TP: {self.TP}')
        print(f'TN: {self.TN}')
        print(f'FP: {self.FP}')
        print(f'FN: {self.FN}')

=======
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
        self.calculate_accuracy()
        self.calculate_recall()
        self.calculate_specificity()
        self.calculate_precision()
        self.calculate_false_discovery_rate()
        self.calculate_f1_score()

<<<<<<< HEAD
        print(f'Accuracy: {self.calculate_accuracy():.3f} %')
        print(f'Recall: {self.calculate_recall():.3f} %')
        print(f'Specificity: {self.calculate_specificity():.3f} %')
        print(f'Precision: {self.calculate_precision():.3f} %')
        print(f'False Discovery Rate: {self.calculate_false_discovery_rate():.3f} %')
        print(f'F1 score: {self.calculate_f1_score():.3f} %')

    def calculate_accuracy(self):
        if self.TP + self.TN + self.FP + self.FN != 0:
            return 100 * ((self.TP + self.TN) / (self.TP + self.TN + self.FP + self.FN))

        print('Division by zero error')
        return -1

    def calculate_precision(self):
        if self.TP + self.FP != 0:
            return 100 * (self.TP / (self.TP + self.FP))
        print('Division by zero error')
        return -1

    def calculate_recall(self):
        if self.TP + self.FN != 0:
            return 100 * (self.TP / (self.TP + self.FN))
        print('Division by zero error')
        return -1

    def calculate_specificity(self):
        if self.TN + self.FP != 0:
            return 100 * (self.TN / (self.TN + self.FP))
        print('Division by zero error')
        return -1

    def calculate_false_discovery_rate(self):
        if self.FP + self.TP != 0:
            return 100 * (self.FP / (self.FP + self.TP))
        print('Division by zero error')
        return -1

    def calculate_f1_score(self):
        if 2 * self.TP + self.FP + self.FN != 0:
            return 100 * ((2 * self.TP) / (2 * self.TP + self.FP + self.FN))
        print('Division by zero error')
        return -1


def preprocess_churn_data(df, label, num_of_features):
    # print("Preprocessing Churn Dataset")
    util = Utility()

    if num_of_features > df.shape[1]:
        print(f'Exceeded total features, using max {df.shape[1]} features')
=======
        print(f'TP: {self.TP}')
        print(f'TN: {self.TN}')
        print(f'FP: {self.FP}')
        print(f'FN: {self.FN}')

        print(f'Accuracy: {self.calculate_accuracy()}')
        print(f'Recall (Sensitivity) : {self.calculate_recall()}')
        print(f'Specificity: {self.calculate_specificity()}')
        print(f'Precision: {self.calculate_precision()}')
        print(f'False Discovery Rate: {self.calculate_false_discovery_rate()}')
        print(f'F1 score: {self.calculate_f1_score()}')

    def calculate_accuracy(self):
        return (self.TP + self.TN) / (self.TP + self.TN + self.FP + self.FN)

    def calculate_precision(self):
        return self.TP / (self.TP + self.FP)

    def calculate_recall(self):
        return self.TP / (self.TP + self.FN)

    def calculate_specificity(self):
        return self.TN / (self.TN + self.FP)

    def calculate_false_discovery_rate(self):
        return self.FP / (self.FP + self.TP)

    def calculate_f1_score(self):
        return (2 * self.TP) / (2 * self.TP + self.FP + self.FN)


def preprocess_churn_data(df, label, num_of_features):
    #print("Preprocessing Churn Dataset")
    util = Utility()

    if num_of_features > df.shape[1]:
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
        num_of_features = df.shape[1]

    # print(len(df))

    df.dropna(axis=0, subset=[label])

    # print(len(df))

    # drop the customer ID column in the dataset
    df.drop('customerID', axis=1, inplace=True)

    # converting the labels(y) to numeric labels
    # print(df.head())
    label_encoder = preprocessing.LabelEncoder()
    df[label] = label_encoder.fit_transform(df[label])
    # print(df.head())

    # print("\nMissing values :  ", df.isnull().sum().values.sum())

    # get the whitespace  counts and remove them
    # util.get_whitespace_count(df)

    df['TotalCharges'] = df['TotalCharges'].replace(r'^\s*$', np.NaN, regex=True)

    # util.get_whitespace_count(df)

    # print("\nMissing values :  ", df.isnull().sum())

    # converting a single column to float
    # df[cols] = df[cols].apply(pd.to_numeric, errors='coerce') where cols are required columns we want to convert
    df['TotalCharges'] = pd.to_numeric(df['TotalCharges'], downcast="float", errors='coerce')

    # print("\nBefore Missing values :  ", df.isnull().sum())

    # replacing the missing values with mean for total charges
    df['TotalCharges'].fillna(value=df['TotalCharges'].mean(), inplace=True)

    # print("\nAfter Missing values :  ", df.isnull().sum())

    if num_of_features != -1:
        # get all columns
        col_name_dict = util.get_all_cols(df)

        # gain computations
        temp_df = df.copy()
        gainFilter = InformationGain(temp_df, col_name_dict['num_cols'], label)
        final_cols = gainFilter.get_final_column_list(num_of_features=num_of_features)

        # print('Final Cols are: ==>')
        # print(final_cols)
        df = df[final_cols]

    # get all columns
    col_name_dict = util.get_all_cols(df)

    # removing categorical columns with mode( most frequent value)
    for cat_col in col_name_dict['cat_cols_with_nan']:
        df[cat_col].fillna(value=df[cat_col].mode()[0], inplace=True)

    # print(df.isnull().sum())

    # removing numerical columns with mean value
    for num_col in col_name_dict['num_cols_with_nan']:
        df[num_col].fillna(value=df[num_col].mean(), inplace=True)

    # print(df.isnull().sum())

    # one hot encoding the categorical cols
    df = pd.get_dummies(df, columns=list(set(col_name_dict['cat_cols']) - set(col_name_dict['binary_cols'])))

    # label encoding the binary cols
    for col in list(set(col_name_dict['binary_cols']) - set(label)):
        # print('Label Encoding: ', col)
        label_encoder = preprocessing.LabelEncoder()
        df[col] = label_encoder.fit_transform(df[col])

    if len(list(set(col_name_dict['num_cols']) - set(col_name_dict['binary_cols']))) != 0:
        df = util.transformStandardScaler(df, list(set(col_name_dict['num_cols']) - set(col_name_dict['binary_cols'])),
                                          label)

    # changing the lables from 0,1 to -1,1
    df[label] = df[label].replace([0], -1)

    df.reset_index(inplace=True, drop=True)
<<<<<<< HEAD
    # print('DONE')
=======
    #print('DONE')
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
    return df


def preprocess_adult_data(train_df, test_df, label, num_of_features):
<<<<<<< HEAD
    util = Utility()

    if num_of_features > train_df.shape[1]:
        print(f'Exceeded total features, using max {train_df.shape[1]} features')
        num_of_features = train_df.shape[1]
=======
    #print('Preprocessing Adult Dataset')
    util = Utility()

    if num_of_features > df.shape[1]:
        print(f'Exceeded total features, using max {df.shape[1]} features')
        num_of_features = df.shape[1]
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a

    test_df['income'].replace(regex=True, to_replace=r'\.', value='', inplace=True)
    test_df.drop([0], inplace=True)

    # dropping native country
    train_df.drop('native-country', inplace=True, axis=1)
    test_df.drop('native-country', inplace=True, axis=1)
    # reset Index
    train_df.reset_index(inplace=True, drop=True)
    test_df.reset_index(inplace=True, drop=True)

    # print info
    # print(df.info())

    # replacing all the ? with Nan
    train_df.replace('?', np.nan, inplace=True)
    test_df.replace('?', np.nan, inplace=True)

    # get whitespace count
    # get_whitespace_count(train_df)
    # get_whitespace_count(test_df)

    # checking if there is null
    # print(train_df.isnull().sum())
    # print(test_df.isnull().sum())

    # # dropping native country
    # df.drop('native-country', inplace=True, axis=1)

    # removing Nan from labels
    train_df.dropna(axis=0, subset=[label])
    test_df.dropna(axis=0, subset=[label])

    # print(train_df[label].unique())
    # print(test_df[label].unique())

    # converting the labels(y) to numeric labels
    label_encoder = preprocessing.LabelEncoder()
    train_df[label] = label_encoder.fit_transform(train_df['income'])
    test_df[label] = label_encoder.fit_transform(test_df['income'])
    # print('Train DF income')
    # train_df['income'].unique()

    # print('Test DF income')
    # test_df['income'].unique()

    # convert age column to integer
    train_df['age'] = pd.to_numeric(train_df['age'], downcast="integer", errors='coerce')
    test_df['age'] = pd.to_numeric(test_df['age'], downcast="integer", errors='coerce')

    train_col_name_dict = util.get_all_cols(train_df)
    test_col_name_dict = util.get_all_cols(test_df)

    # removing categorical columns with mode( most frequent value)
    for cat_col in train_col_name_dict['cat_cols_with_nan']:
        train_df[cat_col].fillna(value=train_df[cat_col].mode()[0], inplace=True)

    for cat_col in test_col_name_dict['cat_cols_with_nan']:
        test_df[cat_col].fillna(value=test_df[cat_col].mode()[0], inplace=True)

    if num_of_features != -1:
        total_df = pd.concat([train_df, test_df])
        total_df.reset_index(inplace=True, drop=True)

        # get all columns
        col_name_dict = util.get_all_cols(total_df)

        # gain computations
        temp_df = total_df.copy()
        gainFilter = InformationGain(temp_df, col_name_dict['num_cols'], label)
        final_cols = gainFilter.get_final_column_list(num_of_features=num_of_features)

        # print('Final Cols are: ==>')
        # print(final_cols)
        train_df = train_df[final_cols]
        test_df = test_df[final_cols]

    # get all columns
    train_col_name_dict = util.get_all_cols(train_df)
    test_col_name_dict = util.get_all_cols(test_df)

    # print(train_df.isnull().sum())
    # print(test_df.isnull().sum())

    # removing numerical columns with mean value
    for num_col in train_col_name_dict["num_cols"]:
        train_df[num_col].fillna(value=train_df[num_col].mean(), inplace=True)

    for num_col in test_col_name_dict["num_cols"]:
        test_df[num_col].fillna(value=test_df[num_col].mean(), inplace=True)

    # print(train_df.isnull().sum())
    # print(test_df.isnull().sum())

    # one hot encoding the categorical cols
    train_df = pd.get_dummies(train_df, columns=list(
        set(train_col_name_dict['cat_cols']) - set(train_col_name_dict['binary_cols'])))

    # label encoding the binary cols
    for col in list(set(train_col_name_dict['binary_cols']) - set(label)):
        label_encoder = preprocessing.LabelEncoder()
        train_df[col] = label_encoder.fit_transform(train_df[col])

    test_df = pd.get_dummies(test_df,
                             columns=list(set(test_col_name_dict['cat_cols']) - set(test_col_name_dict['binary_cols'])))

    # label encoding the binary cols
    for col in list(set(test_col_name_dict['binary_cols']) - set(label)):
        label_encoder = preprocessing.LabelEncoder()
        test_df[col] = label_encoder.fit_transform(test_df[col])

    # standardize the numerical columns only
    if len(list(set(train_col_name_dict['num_cols']) - set(train_col_name_dict['binary_cols']))) != 0:
        train_df = util.transformStandardScaler(train_df, list(
            set(train_col_name_dict['num_cols']) - set(train_col_name_dict['binary_cols'])), label)

    if len(list(set(test_col_name_dict['num_cols']) - set(test_col_name_dict['binary_cols']))) != 0:
        test_df = util.transformStandardScaler(test_df, list(
            set(test_col_name_dict['num_cols']) - set(test_col_name_dict['binary_cols'])), label)

    # changing the scale
    train_df[label] = train_df[label].replace([0], -1)
    test_df[label] = test_df[label].replace([0], -1)

    # print(df.head())
<<<<<<< HEAD
    # print('DONE')
=======
    #print('DONE')
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
    return train_df, test_df


def preprocess_credit_card(df, label, fract_neg_class, num_of_negative_sample, num_of_features):
<<<<<<< HEAD
    # print("Preprocessing Fraud Dataset")
    util = Utility()
    # print(df.info())

    if num_of_features > df.shape[1]:
        print(f'Exceeded total features, using max {df.shape[1]} features')
        num_of_features = df.shape[1]

=======
    #print("Preprocessing Fraud Dataset")
    util = Utility()
    # print(df.info())

>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
    # reset Index
    df.reset_index(inplace=True, drop=True)

    # print info
    # print(df.info())

    # replacing all the ? with Nan
    df.replace('?', np.nan, inplace=True)

    # get whitespace count
    # get_whitespace_count(df)

    # checking ig there is null
    # print(df.isnull().sum())

    # converting the labels(y) to numeric labels
    label_encoder = preprocessing.LabelEncoder()
    df[label] = label_encoder.fit_transform(df[label])

    # print(df.head(20))

    # print(df.dtypes)

    # get all columns
    col_names = util.get_all_cols(df)

    # removing categorical columns with mode( most frequent value)
    for cat_col in col_names['cat_cols_with_nan']:
        df[cat_col].fillna(value=df[cat_col].mode()[0], inplace=True)

    # print(df.isnull().sum())

    # removing numerical columns with mean value
    for num_col in col_names['num_cols_with_nan']:
        df[num_col].fillna(value=df[num_col].mean(), inplace=True)

    # print(df.isnull().sum())

    if num_of_features != -1:
        # gain computations
        temp_df = df.copy()
        gainFilter = InformationGain(temp_df, col_names['num_cols'], label)
        final_cols = gainFilter.get_final_column_list(num_of_features=num_of_features)

        # print('Final Cols are: ==>')
        # print(final_cols)
        # print('Done')
        df = df[final_cols]

    col_names = util.get_all_cols(df)

    # one hot encoding the categorical cols
    df = pd.get_dummies(df, columns=list(set(col_names['cat_cols']) - set(col_names['binary_cols'])))

    # label encoding the binary cols
    for col in list(set(col_names['binary_cols']) - set(label)):
        label_encoder = preprocessing.LabelEncoder()
        df[col] = label_encoder.fit_transform(df[col])

    if len(list(set(col_names['num_cols']) - set(col_names['binary_cols']))) != 0:
        df = util.transformMinMaxScaler(df, list(set(col_names['num_cols']) - set(col_names['binary_cols'])), label)

    # changing the scale
    df[label] = df[label].replace([0], -1)

    # choosing all positive class and less negative classes
    # print(len(df))
    normal_indices = df[df[label] == -1].index
    fraud_indices = df[df[label] == 1].index

    # print('Length of normal_indices', len(normal_indices))
    # print('Length of fraud indices', len(fraud_indices))

    # sanity check
    assert len(normal_indices.tolist() + fraud_indices.tolist()) == len(df)

    if fract_neg_class == -1:
        random_normal_indices = np.random.choice(normal_indices, num_of_negative_sample, replace=False)
    else:
        random_normal_indices = np.random.choice(normal_indices, fract_neg_class * len(fraud_indices), replace=False)

    random_normal_sample_df = df.iloc[random_normal_indices, :]

    fraud_df = df.iloc[fraud_indices, :]

    final_df = pd.concat([random_normal_sample_df, fraud_df])

    final_df.reset_index(inplace=True, drop=True)

    # print(final_df.head(2))
    # print(final_df.shape)
<<<<<<< HEAD
    # print("DONE")
=======
    #print("DONE")
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
    return final_df


class LogisticRegression:
    def __init__(self, learning_rate, max_iter, test_size, early_stop_error, decay) -> None:
        self.learning_rate = learning_rate
        self.max_iter = max_iter
        self.weights = None
        self.cost_history = []
        self.learning_rate_history = []
        self.x_train = None
        self.x_test = None
        self.y_train = None
        self.y_test = None
        self.test_size = test_size
        self.label = None
        self.early_stop_error = early_stop_error
        self.decay = decay

    def split_given_train_test_df(self, train_df, test_df, label):
        self.label = label

        y_train = train_df[label]
        x_train = train_df.drop(label, axis=1)

        self.y_train = np.array(y_train).reshape(y_train.shape[0], 1)
        self.x_train = np.array(x_train)

        y_test = test_df[label]
        x_test = test_df.drop(label, axis=1)

        self.y_test = np.array(y_test).reshape(y_test.shape[0], 1)
        self.x_test = np.array(x_test)

    def split_dataset(self, df, label):

        self.label = label
        y = df[label]
        X = df.drop(label, axis=1)

        y = np.array(y).reshape(y.shape[0], 1)
        X = np.array(X)

        self.x_train, self.x_test, self.y_train, self.y_test = train_test_split(X, y, test_size=self.test_size,
                                                                                random_state=111)

    def fit(self, is_constant_lr, no_curve, calculate_metric_on_train):
        # df[label] = df[label].replace([-1],0)
        n_samples, n_features = self.x_train.shape

        # initializing weights
        # self.uniform_weight_initializer(n_features)
        self.zero_initializer(n_features)

        # initializing cost history
        self.cost_history = np.zeros((self.max_iter, 1))
        self.learning_rate_history = np.zeros((self.max_iter, 1))

        for epoch in range(self.max_iter):
            # print(X.shape, self.weights.shape)
            # print('Epoch: ', epoch)

            if not is_constant_lr:
                # adjusting learning rate in each epoch
                self.learning_rate_scheduler(epoch)
                self.learning_rate_history[epoch] = self.learning_rate

            h_w = np.tanh(np.matmul(self.x_train, self.weights))
            X_T = np.transpose(self.x_train)

            assert h_w.shape == self.y_train.shape

            y_h_w = self.y_train - h_w
            tan_der = 1 - np.square(h_w)

            assert y_h_w.shape == tan_der.shape

            # update the weights here
            self.weights = self.weights + (2 * self.learning_rate) * (1 / n_samples) * np.matmul(X_T, np.multiply(y_h_w,
                                                                                                                  tan_der))

            # calculating mse with updated weights
            cost = self.calculate_mse_cost(y_h_w)
            self.cost_history[epoch] = cost

            # computing 1 - acc for making weak learner
            y_train_pred = self.predict(self.x_train)
            accuracy = accuracy_score(self.y_train, y_train_pred)
            # print(accuracy)

            if 1 - accuracy < self.early_stop_error:
                # print('Stopping Training since the error is less than 0.5')
<<<<<<< HEAD
                # print(1 - accuracy)
=======
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
                break

            # if cost < self.early_stop_error:
            #     print(cost)
            #     print('Stopping Training since the error is less than 0.5')
            #     break

        if not no_curve:
            self.plot_cost_vs_iteration()
            self.plot_learning_rate_curve()

        # calculate metrics on training set
        if calculate_metric_on_train:
            y_pred = self.predict(self.x_train)
            metric = MetricCalculator(self.y_train, y_pred)
            metric.calculate_all_metric()

    def predict(self, x):
        y_pred = np.tanh(x @ self.weights)

        y_pred[y_pred > 0] = 1
        y_pred[y_pred <= 0] = -1

        return y_pred

    def uniform_weight_initializer(self, num_features):
        self.weights = np.random.uniform(low=0, high=1, size=num_features).reshape(num_features, 1)

    def xavier_initialization(self, num_features):
        self.weights = np.full((num_features, 1), np.sqrt(1 / num_features))  # Xavier Initialization

    def zero_initializer(self, num_features):
        self.weights = np.zeros((num_features, 1))

    def generate_metric(self):
        y_pred = self.predict(self.x_test)
        metric = MetricCalculator(self.y_test, y_pred)
        metric.calculate_all_metric()

    @staticmethod
    def calculate_mse_cost(y_h_w):
        return np.mean(np.square(y_h_w))

    def plot_cost_vs_iteration(self):

        print(min(self.cost_history))
        plt.figure()
        plt.plot(range(self.max_iter), self.cost_history)
        plt.title('Cost Function Convergence Curve')
        plt.xlabel("Number of Iterations")
        plt.ylabel("Cost")
        plt.show()

    def plot_learning_rate_curve(self):
        plt.figure()
        plt.plot(range(self.max_iter), self.learning_rate_history)
        plt.title('Learning Rate Decay Curve')
        plt.xlabel("Number of Iterations")
        plt.ylabel("Learning Rate")
        plt.show()

    def learning_rate_scheduler(self, epoch):
        self.learning_rate *= (1. / (1. + self.decay * epoch))


class Adaboost:

    def __init__(self, num_of_learner, test_size):
        self.test_size = test_size
        self.y_test = None
        self.y_train = None
        self.x_test = None
        self.x_train = None
        self.num_of_learner = num_of_learner
        self.W = []
        self.h = []
        self.z = []
        self.label = None
        self.train_df = None
        self.test_df = None

    def split_dataset(self, df, label):
        self.label = label

        self.train_df, self.test_df = train_test_split(df, test_size=self.test_size, random_state=111)

    def split_dataset_given_tran_test(self, train_df, test_df, label):
        self.label = label
        self.train_df = train_df
        self.test_df = test_df

    def convert_df_to_np(self, df):
        y = df[self.label]
        X = df.drop(self.label, axis=1)

        y = np.array(y).reshape(y.shape[0], 1)
        X = np.array(X)

        return X, y

    def fit(self, base_learner_max_iter, error, learning_rate, decay, var_lr):

        # setting the weights
        N = self.train_df.shape[0]
        self.W = [1.0 / N] * N
        self.h = [None] * self.num_of_learner
        self.z = [None] * self.num_of_learner

        # keeping a copy of the original train dataframe
        original_train_df = self.train_df.copy()

        # getting numpy arrays for original train dataframe
        X_original_train_df, y_original_train_df = self.convert_df_to_np(original_train_df)

        for k in range(self.num_of_learner):
            # print('Boosting Round: ', k)
            lgr_learner = LogisticRegression(learning_rate, base_learner_max_iter, 0.2, error, decay)

            resampled_df = original_train_df.sample(n=N, weights=self.W, replace=True, random_state=111)

            # convert resampled_df to fit to logistic regression
            X, y = self.convert_df_to_np(resampled_df)

            lgr_learner.x_train = X
            lgr_learner.y_train = y

            # train the weak learner
            lgr_learner.fit(is_constant_lr=var_lr, no_curve=True, calculate_metric_on_train=False)

            # storing the weak learner
            self.h[k] = lgr_learner

            error = 0.0

            # getting the prediction of the weak learner for the original training data
            y_pred = self.h[k].predict(X_original_train_df)

            for i in range(N):
                if y_pred[i] != y_original_train_df[i]:
                    error += self.W[i]

            if error > 0.5:
                self.z[k] = 0
                continue

            for i in range(N):
                if y_pred[i] == y_original_train_df[i]:
                    self.W[i] = (self.W[i] * error) / (1.0 - error)

            # normalize data weights
            self.normalize_data_weights()

            if error == 0:
                self.z[k] = np.log2(float('inf'))
            else:
                self.z[k] = np.log2((1.0 - error) / error)

        # calculate metric for the training set and print
        self.weighted_sum(X_original_train_df, y_original_train_df)

    def predict(self):
        self.x_test, self.y_test = self.convert_df_to_np(self.test_df)
        self.weighted_sum(self.x_test, self.y_test)

    @staticmethod
    def majority_voting(preds):
        axis = 1
        unique_vals, indices = np.unique(preds, return_inverse=True)
        return unique_vals[np.argmax(np.apply_along_axis(np.bincount, axis, indices.reshape(preds.shape),
                                                         None, np.max(indices) + 1), axis=axis)]

    def weighted_sum(self, x, y):

        preds = []
        for k in range(self.num_of_learner):
            weak_learner_weights = self.h[k].weights
            weak_learner_preds = np.tanh(x @ weak_learner_weights)
            weighted_preds = self.z[k] * weak_learner_preds
            preds.append(weighted_preds)

        preds = np.array(preds).squeeze().T
        # print(preds.shape)

        weighted_sum_result = np.sum(preds, axis=1)
        weighted_sum_result[weighted_sum_result > 0] = 1
        weighted_sum_result[weighted_sum_result <= 0] = -1

        # metric = MetricCalculator(y, weighted_sum_result)
        # metric.calculate_all_metric()
<<<<<<< HEAD
        accuracy = 100 * accuracy_score(y_true=y, y_pred=weighted_sum_result)
        print(f'Accuracy: {accuracy:.3f} %')
=======
        accuracy = accuracy_score(y_true=y, y_pred=weighted_sum_result)
        print(f'Accuracy: {accuracy}')
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a

    def normalize_data_weights(self):
        total_data_weight = sum(self.W)
        W = [(data_weight / total_data_weight) for data_weight in self.W]
        self.W = W


<<<<<<< HEAD
def preprocess_bank_data(df, label, num_of_features):
    util = Utility()
    df.dropna(axis=0, subset=[label])

    label_encoder = preprocessing.LabelEncoder()
    df[label] = label_encoder.fit_transform(df[label])

    if num_of_features != -1:
        # get all columns
        col_name_dict = util.get_all_cols(df)

        # gain computations
        temp_df = df.copy()
        gainFilter = InformationGain(temp_df, col_name_dict['num_cols'], label)
        final_cols = gainFilter.get_final_column_list(num_of_features=num_of_features)

        # print('Final Cols are: ==>')
        # print(final_cols)
        df = df[final_cols]

    col_name_dict = util.get_all_cols(df)

    # removing categorical columns with mode( most frequent value)
    for cat_col in col_name_dict['cat_cols_with_nan']:
        df[cat_col].fillna(value=df[cat_col].mode()[0], inplace=True)

    print(df.isnull().sum())

    # removing numerical columns with mean value
    for num_col in col_name_dict['num_cols_with_nan']:
        df[num_col].fillna(value=df[num_col].mean(), inplace=True)

    df = pd.get_dummies(df, columns=list(set(col_name_dict['cat_cols']) - set(col_name_dict['binary_cols'])))

    # label encoding the binary cols
    for col in list(set(col_name_dict['binary_cols']) - set(label)):
        # print('Label Encoding: ', col)
        label_encoder = preprocessing.LabelEncoder()
        df[col] = label_encoder.fit_transform(df[col])

    if len(list(set(col_name_dict['num_cols']) - set(col_name_dict['binary_cols']))) != 0:
        df = util.transformStandardScaler(df,
                                          list(set(col_name_dict['num_cols']) - set(col_name_dict['binary_cols'])),
                                          label)

    # changing the lables from 0,1 to -1,1
    df[label] = df[label].replace([0], -1)

    df.reset_index(inplace=True, drop=True)
    # print('DONE')
    return df


def run_churn_lgr(df, label, max_iter, lr):
    print('Logistic Regression Training Metric (Churn Dataset)')
    lgr = LogisticRegression(learning_rate=lr, max_iter=max_iter, test_size=0.2, early_stop_error=0, decay=5e-6)
=======
def run_churn_lgr(df, label):
    print('Logistic Regression Training Metric (Churn Dataset)')
    lgr = LogisticRegression(learning_rate=0.1, max_iter=1000, test_size=0.2, early_stop_error=0, decay=5e-6)
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
    lgr.split_dataset(df, label)
    lgr.fit(is_constant_lr=False, no_curve=True, calculate_metric_on_train=True)

    print('\nLogistic Regression Testing Metric (Churn Dataset)')
    lgr.generate_metric()
    print()


<<<<<<< HEAD
def run_churn_adaboost(df, label, boosting_rounds, learner_miter, learner_lr, learner_estop):
    print('Adaboost Training Metric (Churn Dataset)')
    adaboost_classifier = Adaboost(num_of_learner=boosting_rounds, test_size=0.2)
    adaboost_classifier.split_dataset(df=df, label=label)
    adaboost_classifier.fit(base_learner_max_iter=learner_miter, error=learner_estop, learning_rate=learner_lr,
                            decay=5e-6, var_lr=False)
=======
def run_churn_adaboost(df, label, boosting_rounds):
    print('Adaboost Training Metric (Churn Dataset)')
    adaboost_classifier = Adaboost(num_of_learner=boosting_rounds, test_size=0.2)
    adaboost_classifier.split_dataset(df=df, label=label)
    adaboost_classifier.fit(base_learner_max_iter=1000, error=0.5, learning_rate=0.1, decay=5e-6, var_lr=False)
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a

    print('\nAdaboost Testing Metric (Churn Dataset)')
    adaboost_classifier.predict()
    print()


<<<<<<< HEAD
def run_adult_lgr(train_df, test_df, label, max_iter, lr):
    print('Logistic Regression Training Metric (Adult Income Dataset)')
    lgr = LogisticRegression(learning_rate=lr, max_iter=max_iter, test_size=0.2, early_stop_error=0, decay=6e-6)
=======
def run_adult_lgr(train_df, test_df, label):
    print('Logistic Regression Training Metric (Adult Income Dataset)')
    lgr = LogisticRegression(learning_rate=0.1, max_iter=1000, test_size=0.2, early_stop_error=0, decay=6e-6)
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
    lgr.split_given_train_test_df(train_df, test_df, label)
    lgr.fit(is_constant_lr=True, no_curve=True, calculate_metric_on_train=True)

    print('\nLogistic Regression Testing Metric (Adult Income Dataset)')
    lgr.generate_metric()
    print()


<<<<<<< HEAD
def run_adult_adaboost(train_df, test_df, label, boosting_rounds, learner_miter, learner_lr, learner_estop):
    print('Adaboost Training Metric (Adult Income Dataset)')
    adaboost_classifier = Adaboost(num_of_learner=boosting_rounds, test_size=0.2)
    adaboost_classifier.split_dataset_given_tran_test(train_df, test_df, label)
    adaboost_classifier.fit(base_learner_max_iter=learner_miter, error=learner_estop, learning_rate=learner_lr,
                            decay=5e-6, var_lr=False)
=======
def run_adult_adaboost(train_df, test_df, label, boosting_rounds):
    print('Adaboost Training Metric (Adult Income Dataset)')
    adaboost_classifier = Adaboost(num_of_learner=boosting_rounds, test_size=0.2)
    adaboost_classifier.split_dataset_given_tran_test(train_df, test_df, label)
    adaboost_classifier.fit(base_learner_max_iter=1000, error=0.5, learning_rate=0.1, decay=5e-6, var_lr=False)
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a

    print('\nAdaboost Testing Metric (Adult Income Dataset)')
    adaboost_classifier.predict()
    print()


<<<<<<< HEAD
def run_fraud_lgr(df, label, max_iter, lr):
    print('Logistic Regression Training Metric (Fraud Dataset)')
    lgr = LogisticRegression(learning_rate=lr, max_iter=max_iter, test_size=0.2, early_stop_error=0, decay=6e-6)
=======
def run_fraud_lgr(df, label):
    print('Logistic Regression Training Metric (Fraud Dataset)')
    lgr = LogisticRegression(learning_rate=0.1, max_iter=1000, test_size=0.2, early_stop_error=0, decay=6e-6)
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
    lgr.split_dataset(df, label)
    lgr.fit(is_constant_lr=True, no_curve=True, calculate_metric_on_train=True)

    print('\nLogistic Regression Testing Metric (Fraud Dataset)')
    lgr.generate_metric()
    print()


<<<<<<< HEAD
def run_fraud_adaboost(df, label, boosting_rounds, learner_miter, learner_lr, learner_estop):
    print('Adaboost Training Metric (Fraud Dataset)')
    adaboost_classifier = Adaboost(num_of_learner=boosting_rounds, test_size=0.2)
    adaboost_classifier.split_dataset(df, label=label)
    adaboost_classifier.fit(base_learner_max_iter=learner_miter, error=learner_estop, learning_rate=learner_lr,
                            decay=5e-6, var_lr=False)
=======
def run_fraud_adaboost(df, label, boosting_rounds):
    print('Adaboost Training Metric (Fraud Dataset)')
    adaboost_classifier = Adaboost(num_of_learner=boosting_rounds, test_size=0.2)
    adaboost_classifier.split_dataset(df, label=label)
    adaboost_classifier.fit(base_learner_max_iter=1000, error=0.5, learning_rate=0.1, decay=5e-6, var_lr=False)
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a

    print('\nAdaboost Testing Metric (Fraud Dataset)')
    adaboost_classifier.predict()
    print()


<<<<<<< HEAD
def run_churn_all(df, label, boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr, ab_learner_estop):
    run_churn_lgr(df, label, lgr_miter, lgr_lr)
    run_churn_adaboost(df, label, boosting_rounds, ab_learner_miter, ab_learner_lr, ab_learner_estop)


def run_adult_all(train_df, test_df, label, boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr,
                  ab_learner_estop):
    run_adult_lgr(train_df, test_df, label, lgr_miter, lgr_lr)
    run_adult_adaboost(train_df, test_df, label, boosting_rounds, ab_learner_miter, ab_learner_lr, ab_learner_estop)


def run_fraud_all(df, label, boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr, ab_learner_estop):
    run_fraud_lgr(df, label, lgr_miter, lgr_lr)
    run_fraud_adaboost(df, label, boosting_rounds, ab_learner_miter, ab_learner_lr, ab_learner_estop)


def run_bank_lgr(df, label, max_iter, lr):
    print('Logistic Regression Training Metric (Bank dataset)')
    lgr = LogisticRegression(learning_rate=lr, max_iter=max_iter, test_size=0.1, early_stop_error=0, decay=6e-6)
    lgr.split_dataset(df, label)
    lgr.fit(is_constant_lr=True, no_curve=True, calculate_metric_on_train=True)

    print('\nLogistic Regression Testing Metric (Bank Dataset)')
    lgr.generate_metric()
    print()


def run_bank_adaboost(df, label, boosting_rounds, learner_miter, learner_lr, learner_estop):
    print('Adaboost Training Metric (Bank Dataset)')
    adaboost_classifier = Adaboost(num_of_learner=boosting_rounds, test_size=0.1)
    adaboost_classifier.split_dataset(df, label=label)
    adaboost_classifier.fit(base_learner_max_iter=learner_miter, error=learner_estop, learning_rate=learner_lr,
                            decay=5e-6, var_lr=False)

    print('\nAdaboost Testing Metric (Bank Dataset)')
    adaboost_classifier.predict()
    print()


def run_bank_all(df, label, boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr,
                 ab_learner_estop):
    run_bank_lgr(df, label, lgr_miter, lgr_lr)
    run_bank_adaboost(df, label, boosting_rounds, ab_learner_miter, ab_learner_lr, ab_learner_estop)
=======
def run_churn_all(df, label, boosting_rounds):
    run_churn_lgr(df, label)
    run_churn_adaboost(df, label, boosting_rounds)


def run_adult_all(train_df, test_df, label, boosting_rounds):
    run_adult_lgr(train_df, test_df, label)
    run_adult_adaboost(train_df, test_df, label, boosting_rounds)


def run_fraud_all(df, label, boosting_rounds):
    run_fraud_lgr(df, label)
    run_fraud_adaboost(df, label, boosting_rounds)
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a


if __name__ == '__main__':
    # Create the parser
    parser = argparse.ArgumentParser(
        description='arguments to parse')

    # Add the arguments
    parser.add_argument('--churn',
                        metavar='churn',
                        type=str,
                        help='the path to the churn dataset')

    parser.add_argument('--adult',
                        metavar='adult',
                        type=str,
                        help='the path to the adult train and test dataset (folder of the dataset)')

    parser.add_argument('--fraud',
                        metavar='fraud',
                        type=str,
                        help='the path to the fraud dataset')

<<<<<<< HEAD
    parser.add_argument('--bank',
                        metavar='bank',
                        type=str,
                        help='the path to the bank dataset')

=======
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
    parser.add_argument('--dataset',
                        metavar='dataset',
                        type=int,
                        help='please give input which dataset to run as (0,1,2,3) - 0 for all')

    parser.add_argument('--featnum',
                        metavar='featnum',
                        type=int,
                        help='top k feature to filter with IGain')

    parser.add_argument('--brounds',
                        metavar='brounds',
                        type=int,
<<<<<<< HEAD
                        help='Boosting Rounds')

    parser.add_argument('--lgr_miter',
                        metavar='lgr_miter',
                        type=int,
                        help='max iteration of logistic regression')

    parser.add_argument('--lgr_lr',
                        metavar='lgr_lr',
                        type=float,
                        help='learning rate of logistic regression')

    parser.add_argument('--ab_learner_miter',
                        metavar='ab_learner_miter',
                        type=int,
                        help='Adaboost weak learner max iteration')

    parser.add_argument('--ab_learner_lr',
                        metavar='ab_learner_lr',
                        type=float,
                        help='Adaboost weak learner learning rate')

    parser.add_argument('--ab_learner_estop',
                        metavar='ab_learner_estop',
                        type=float,
                        help='Adaboost weak learner early stop')
=======
                        help='number of boosting rounds')
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a

    # Execute the parse_args() method
    args = parser.parse_args()

    churn_data_path = args.churn
    adult_data_path = args.adult
    fraud_data_path = args.fraud
<<<<<<< HEAD
    bank_data_path = args.bank
    dataset_to_use = args.dataset
    num_of_feature = args.featnum
    boosting_rounds = args.brounds
    lgr_miter = args.lgr_miter
    lgr_lr = args.lgr_lr
    ab_learner_miter = args.ab_learner_miter
    ab_learner_lr = args.ab_learner_lr
    ab_learner_estop = args.ab_learner_estop
=======
    dataset_to_use = args.dataset
    num_of_feature = args.featnum
    boosting_rounds = args.brounds
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a

    # preprocess churn dataset
    df = pd.read_csv(churn_data_path)
    df = preprocess_churn_data(df=df, label='Churn', num_of_features=num_of_feature)

    # preprocess adult dataset
    columns = ['age', 'workclass', 'fnlwgt', 'education', 'education-num', 'marital-status', 'occupation',
               'relationship',
               'race', 'sex', 'capital-gain', 'capital-loss', 'hours-per-week', 'native-country', 'income']
<<<<<<< HEAD
    train_df = pd.read_csv(adult_data_path + '/adult.data', names=columns, header=None, sep=", ", engine='python')
    test_df = pd.read_csv(adult_data_path + '/adult.test', names=columns, header=None, sep=", ", engine='python')
    train_df, test_df = preprocess_adult_data(train_df=train_df, test_df=test_df, label='income',
                                              num_of_features=num_of_feature)
=======
    train_df = pd.read_csv(adult_data_path+'/adult.data', names=columns, header=None, sep=", ", engine='python')
    test_df = pd.read_csv(adult_data_path + '/adult.test', names=columns, header=None, sep=", ", engine='python')
    train_df, test_df = preprocess_adult_data(train_df=train_df, test_df=test_df, label='income', num_of_features=num_of_feature)
>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a

    # preprocess credit card fraud dataset
    crd_fraud_df = pd.read_csv(fraud_data_path)
    crd_fraud_df = preprocess_credit_card(crd_fraud_df, 'Class', fract_neg_class=-1, num_of_negative_sample=5000,
                                          num_of_features=num_of_feature)

<<<<<<< HEAD
    bank_df = pd.read_csv(bank_data_path, sep=";")
    bank_df = preprocess_bank_data(bank_df, 'y', 10)

    if dataset_to_use == 0:
        run_churn_all(df, 'Churn', boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr,
                      ab_learner_estop)
        run_adult_all(train_df, test_df, 'income', boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr,
                      ab_learner_estop)
        run_fraud_all(crd_fraud_df, 'Class', boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr,
                      ab_learner_estop)
    elif dataset_to_use == 1:
        run_churn_all(df, 'Churn', boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr,
                      ab_learner_estop)
    elif dataset_to_use == 2:
        run_adult_all(train_df, test_df, 'income', boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr,
                      ab_learner_estop)
    elif dataset_to_use == 3:
        run_fraud_all(crd_fraud_df, 'Class', boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr,
                      ab_learner_estop)
    elif dataset_to_use == 4:
        run_bank_all(bank_df, 'y', boosting_rounds, lgr_miter, lgr_lr, ab_learner_miter, ab_learner_lr,
                     ab_learner_estop)
=======
    if dataset_to_use == 0:
        run_churn_all(df, 'Churn', boosting_rounds)
        run_adult_all(train_df, test_df, 'income', boosting_rounds)
        run_fraud_all(crd_fraud_df, 'Class', boosting_rounds)
    elif dataset_to_use == 1:
        run_churn_all(df, 'Churn', boosting_rounds)
    elif dataset_to_use == 2:
        run_adult_all(train_df, test_df, 'income', boosting_rounds)
    else:
        run_fraud_all(crd_fraud_df, 'Class', boosting_rounds)

>>>>>>> 4ac3c13078168c5aa2e607a73f0c6526fc7a234a
