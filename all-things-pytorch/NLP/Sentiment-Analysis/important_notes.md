## Steps for evaluation

* Take the model to evaluation mode

  * ```python
    net.eval()
    ```

  * Tokenize the test sentence in the same way, and convert the numbers to integers. return an array of array of the integer tokens.

  * Perform required padding to the sequences with previously defined method

  * Extract the batch size

  * Convert the feature to tensor, and take it to cuda if you have GPU

  * Initialize the **LSTM/GRU** hidden state

  * Evaluate the model, like you did in training

  * Convert the output to certain format by **squeeze()** to remove the extra dimension and rounding the value

  * Perform prediction.