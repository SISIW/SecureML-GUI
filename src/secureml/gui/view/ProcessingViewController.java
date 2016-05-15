/*
 * Mike Nickels
 * 
 * developed for
 * University of Washington, Tacoma
 * Privacy Preserving Maching Learning Group
 * secureml.insttech.washington.edu
 */

package secureml.gui.view;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import secureml.securesvm.PrivateSVMClient;
import secureml.svm.SVMClient;

/**
 * Controller for the ResultView.fxml Scene.
 * 
 * @author Mike Nickels | mnickels@uw.edu
 * @version 0.5
 */
public class ProcessingViewController extends Controller {

	private String inputText;
	private Image inputImage;
	private boolean secure;
	private String outputText;

	@FXML
	private TextArea bytesTextArea;
	@FXML
	private TextArea charsTextArea;
	@FXML
	Button continueButton;

	public void setInputs(String inputText, boolean secure, Image inputImage) {

		this.inputText = inputText;
		setbytesText(inputText);
		this.secure = secure;
		this.inputImage = inputImage;
	}

	public void analyzeData(ArrayList<Double> features) {
		// TODO incorporate the methods of analysis on inputText and inputImage here!
		SVMClient svmClient = new SVMClient();
		for (int i = 0; i < features.size(); i++) {
			System.out.print(features.get(i) + ",");
		}
		String result = svmClient.predict(features);
		System.out.println(result);
		this.outputText = result;
		continueButton.setDisable(false);
	}

	public void secureAnalyzeData(double[] features) {
		// TODO incorporate the methods of analysis on inputText and inputImage here!
		outputText = "";
		new Thread() {
			public void run() {
				boolean[] results = PrivateSVMClient.globalClient.run(features, bytesTextArea);
				outputText = results[0] + " " + results[1] + " " + results[2] + " " + 
						results[3] + " " + results[4];
				bytesTextArea.appendText("\n<<< Classification Complete >>>\n");
				continueButton.setDisable(false);
			}
		}.start();
	}

	/**
	 * Sets the text of {@link #outputTextArea}.
	 * @param text the results of the program to output to the user.
	 */
	public void setbytesText(String text) {
		bytesTextArea.setText(text);
	}

	@FXML
	private void seeTheResults() {
		// TODO put the output text in as a parameter of resultView() (or change the parameters for the method if you want to pass it something other than a string)
		mainApp.resultView(this.outputText, "male", "44+", inputImage);
	}

}
