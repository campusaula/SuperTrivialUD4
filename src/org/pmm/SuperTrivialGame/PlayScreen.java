package org.pmm.SuperTrivialGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlayScreen extends BaseSuperTrivialActivity {
	
	final static public int NO_QUESTIONS_FOR_PREFERENCES_DIALOG = 1;
	final static public int NEXT_QUESTION = 2; // Id del diálogo para pasar a la siguiente cuestión. Se le pasará al método showMeDialog(id) y lo recibirá el método onCreateDialog(id)
	final static public int TIME_OUT = 3; // Id del diálogo cuando finaliza el tiempo de una cuestión. Se le pasará al método showMeDialog(id) y lo recibirá el método onCreateDialog(id)
	final static public int END_GAME = 4; // Id del diálogo cuando se acaba el juego. Se le pasará al método showMeDialog(id) y lo recibirá el método onCreateDialog(id)

	final static public int DECREMENT_TIME = 10;
	final static public int TIME_OUT_UPDATE = 11;

	ProgressBar timeProgressBar = null;
	QuestionTimerTask timerTask = null;
	
	List<Question> questionsList = null; // Lista dinámica de Questiones
	Question question = null; // Question actual que está mostrando.
	TextView questionText = null;
	Button[] answerButton = null;

	ColorStateList colors = null; // Colors for the different states (normal, selected, focused) of the TextView.
	
	Iterator<Question> iterator = null; // Iterator necesario para recorrer el List de Question.
	
	int currentScore;
	int userAnswer;
	boolean usedHelp;
	TextView currentScoreText = null;
	int decrementStep;
	int maxTime;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playscreen);
				
		questionsList = generateQuestionsList(); // Relleno el ArrayList de preguntas predefinidas por mi.
		iterator = questionsList.iterator(); // Obtengo un Iterator para recorrer la lista de preguntas.

		if (iterator.hasNext()) { // Creo toda la estructura de la interfaz, pero antes miro si hay preguntas. Devuelve true si el iterator tiene más elementos.
			maxTime = getResources().getInteger(R.integer.MaxTime); // Obtengo el máximo tiempo: 7000ms.
			decrementStep = getResources().getInteger(R.integer.DecrementStep); // Obtengo el tiempo de decremento: 100ms.
			usedHelp = false;
			userAnswer = 0; // Las respuestas de la pregunta empiezan en 1. El cero indica que no hay ninguna respuesta puesta.
			currentScore = 0;
			currentScoreText = (TextView) findViewById(R.id.tvCurrentScore);
			currentScoreText.setText(String.valueOf(currentScore)); // Pongo el valor de mi puntuación actual. String.valueOf(int) convierte un entero a cadena de texto.
			
			questionText = (TextView) findViewById(R.id.tvQuestion);
			answerButton = new Button[]{ // Creo los 4 botones de respuesta recuperándolos del XML.
					(Button) findViewById(R.id.bAnswer1),
					(Button) findViewById(R.id.bAnswer2),
					(Button) findViewById(R.id.bAnswer3),
					(Button) findViewById(R.id.bAnswer4)
			};
			// Les asigno un Listener a cada uno de los botones de respuesta.
			answerButton[0].setOnClickListener(manageAnswer);
			answerButton[1].setOnClickListener(manageAnswer);
			answerButton[2].setOnClickListener(manageAnswer);
			answerButton[3].setOnClickListener(manageAnswer);

			colors = answerButton[0].getTextColors(); // Gets the text colors for the different states (normal, selected, focused) of the TextView.
			
			timeProgressBar = (ProgressBar) findViewById(R.id.pbTime);
			
			nextQuestion();
		}
		else { // Como está en el onCreate, si entra aquí quiere decir que no hay preguntas para esa preferencia ya que o hay preguntas nada más comenzar la actividad.
			showMeDialog(NO_QUESTIONS_FOR_PREFERENCES_DIALOG);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.playoptionsmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case R.id.options_play_help:
			getButtonByAnswerNumber(question.getHelp()).setEnabled(false); // Ponemos a deshabilitado el botón que habíamos marcado como el de ayuda.
			usedHelp = true;
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void nextQuestion() {
		// TODO Auto-generated method stub
		// La primera vez que entre, cargará la primera pregunta.
		// A continuación restauramos los colores de los botones si anteriormente se ha contestado a alguna, y restauramos el botón de la respuesta de ayuda si se ha utilizado.
		if (question != null) { // Aquí entrará a partir de la segunda pregunta. En la primera pregunta aquí no entrará ya que question==null
			getButtonByAnswerNumber(question.getRightAnswer()).setTextColor(colors); // Dejamos el color por defecto de la respuesta correcta que anteriormente se habrá coloreado de verde.
			if ((userAnswer != question.getRightAnswer()) && (timerTask.isStopped())) {
				getButtonByAnswerNumber(userAnswer).setTextColor(colors); // Dejamos el color por defecto de la respuesta contestada que en este caso no ha sido la correcta, para que esté como las demás y podamos seguir jugando.
			}
			if (usedHelp) { // Si ha utilizado la ayuda, habilitamos dicho botón que se ha deshabilitado antes.
				getButtonByAnswerNumber(question.getHelp()).setEnabled(true);
				usedHelp = false;
			}
		}
		
		userAnswer = 0;
		
		question = (Question) iterator.next(); // Recuperamos la siguiente question.
		questionText.setText(question.getQuestionText()); // Cargo el texto de la question en el TextView
		// Ahora pongo para cada botón el texto de las respuestas que contiene el objeto question.
		answerButton[0].setText(question.getAnswers()[0]);		
		answerButton[1].setText(question.getAnswers()[1]);		
		answerButton[2].setText(question.getAnswers()[2]);
		answerButton[3].setText(question.getAnswers()[3]);
	
		//Thread thread = new Thread(timer);
		//thread.start();
		// Empieza la cuentra atrás para dicha pregunta.
		timerTask = new QuestionTimerTask(); // Para cada question creamos un objeto QUestionTimerTask (extiende de AsycTask) nuevo ya que realmente aun está en ejecución.
		// Si no creáramos uno objeto QuestionTImerTask nuevo, nos fallaría, al estar la anterior tarea asíncrona en marcha, ya que no la hemos cancelado.
		// Otra opción sería cancelar dicha tarea asíncrona (timerTask.cancel())
		timerTask.execute();
	}

	protected void showMeDialog(int id) {
		
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = null;
		switch(id) {
			case NO_QUESTIONS_FOR_PREFERENCES_DIALOG:
				builder = new AlertDialog.Builder(this);
				builder.setMessage("There are no questions available for your selected preferences!");
				// Añadimos el botón "Sí" junto con su listener
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						PlayScreen.this.finish(); // Destruimos la actividad.
					}
				});
				break;
			case NEXT_QUESTION:
				builder = new AlertDialog.Builder(this);
				builder.setMessage("Are you ready for the next question?");
				// Añadimos el botón "Sí" junto con su listener
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						nextQuestion();
					}
				});
				break;
			case TIME_OUT:
				builder = new AlertDialog.Builder(this);
				builder.setMessage("TIME OUT!!!\n\nAre you ready for the next question?");
				// Añadimos el botón "Sí" junto con su listener
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						nextQuestion();
					}
				});
				break;
			case END_GAME:
				builder = new AlertDialog.Builder(this);
				builder.setMessage("That was the last question.\nYou scored " + currentScore + " points!");
				// Añadimos el botón "Sí" junto con su listener
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						PlayScreen.this.finish(); // Destruimos la actividad.
					}
				});
				break;
		}
		builder.create().show();
	}
	
	
	private List<Question> generateQuestionsList() { // Método que genera Preguntas ya definidas por nosotros, y las devuelve en un ArrayList
		// TODO Auto-generated method stub
		// Uso Generics, es decir, pongo <Question> para indicar que la lista contiene objetos de tipo <Question>
		List<Question> list = new ArrayList<Question>(); // List es una interface. ArrayList es una clase que implementa dicha Interface.
		
		// Ahora creo unas cuantas cuestiones por defecto.
		Question question = new Question(); // Clase creada por mi.
		question.setSubject("Literature");
		question.setQuestionText("Who wrote the Dunwich Horror?");
		question.setAnswers(new String[]{
			"August Derleth",
			"Howard Phillips Lovecraft",
			"Edgard Allan Poe",
			"Clark Ashton Smith"}
		);
		question.setRightAnswer(2); // Las respuestas de la pregunta empiezan en 1.
		question.setHelp(3); // Es el comodín. Cuando el usuario da al menú y selecciona la opción Help, la app elimina una respuesta que no es correcta.
		list.add(question);
		
		question = new Question();
		question.setSubject("Sports");
		question.setQuestionText("Whos is the current 100 meters men's world record holder?");
		question.setAnswers(new String[]{
			"Usain Bolt",
			"Tyson Gay",
			"Asafa Powell",
			"Nesta Carter"}
		);
		question.setRightAnswer(1);
		question.setHelp(4);
		list.add(question);
		
		question = new Question();
		question.setSubject("History");
		question.setQuestionText("The name of which city was changed to Petrograd and Leningrad?");
		question.setAnswers(new String[]{
			"Moscow",
			"Tashkent",
			"Kiev",
			"St. Petersburg"}
		);
		question.setRightAnswer(4);
		question.setHelp(1);
		list.add(question);

		question = new Question();
		question.setSubject("Science");
		question.setQuestionText("What is the meaning of elephant in Latin?");
		question.setAnswers(new String[]{
			"Long pole",
			"Huge arch",
			"Palm tree",
			"Mountain"}
		);
		question.setRightAnswer(2);
		question.setHelp(1);
		list.add(question);
	
		question = new Question();
		question.setSubject("Movies");
		question.setQuestionText("For which film did Audrey Hepburn get Academy Award for Best Actress?");
		question.setAnswers(new String[]{
			"Roman Holiday",
			"Wait Until Dark",
			"Sabrina",
			"Funny Face"}
		);
		question.setRightAnswer(1);
		question.setHelp(2);
		list.add(question);

		return list;
	}


	// IMPLEMENTATION USING ASYNCTASK
	private class QuestionTimerTask extends AsyncTask<Void, Integer, Boolean> {

		private int currentTime = maxTime;
		private boolean stopTimer = false;

		public int getCurrentTime() { // Para devolver el tiempo restante y contabilizarlo como puntuación.
			return currentTime;
		}
		
		public void stopTimer() {
			stopTimer = true;
		}

		public boolean isStopped() {
			return stopTimer;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			currentTime = maxTime;
			timeProgressBar.setProgress(currentTime);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			while ((currentTime > 0) && (!isCancelled()) && (!isStopped())) {
				try {
					Thread.sleep(decrementStep);
					currentTime = currentTime - decrementStep;
					publishProgress(DECREMENT_TIME);
				}
				catch(InterruptedException ie) {
					Log.d("AsyncTask", "InterruptedException");
				}
			}
			if (!isStopped() && (!isCancelled())) { // Se ha salido del bucle con la condición currentTime <= 0. SE HA ACABADO EL TIEMPO!
				publishProgress(TIME_OUT_UPDATE);
			}
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			switch(values[0]){
				case DECREMENT_TIME:
					//timeProgressBar.incrementProgressBy(0 - decrementStep); // Actualizo la barra de progreso con un valor negativo, lo cual restará.
					timeProgressBar.setProgress(currentTime); // Alternativa a la anterior.
					break;
				case TIME_OUT_UPDATE:
					checkRightAnswer(); // Se ha acabado el tiempo. Compruebo la respuesta marcada, si es que la hay.
					break;
			}
			
		}
		
	}
	
	/* IMPLEMENTATION USING THREADS

	final static public int DECREMENT_TIME_100 = 1;
	final static public int DECREMENT_TIME_2500 = 2;
	 
	private class QuestionTimer implements Runnable {

		boolean stop = false;
		
		public QuestionTimer() {
			// TODO Auto-generated constructor stub
			stop = false;
		}
		
		public void stopTimer() {
			stop = true;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int currentTime = 10000; // 10 seconds expressed in milliseconds
			stop = false;
			
			timeProgressBar.setProgress(currentTime);
			while ((currentTime > 0) && (!stop)) {
				try {
					//Thread.currentThread().join(100);
					Thread.sleep(100);
					timeHandler.sendMessage(timeHandler.obtainMessage(DECREMENT_TIME_100));
				}
				catch(InterruptedException ie) {
					Toast.makeText(PlayScreen.this, "INTERRUPTION", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	QuestionTimer timer = new QuestionTimer();
	
	Handler timeHandler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DECREMENT_TIME_100:
				timeProgressBar.incrementProgressBy(-100);
				return;
			case DECREMENT_TIME_2500:
				timeProgressBar.incrementProgressBy(-2500);
				return;
			}
		};
		
	};
	*/
	
	private OnClickListener manageAnswer = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			timerTask.stopTimer(); //Paramos el tiempo para comprobar si la respuesta seleccionada es correcta o no.
			//timerTask.cancel(true);
			// Devolvemos la opción marcada por el usuario, para ello miramos sobre qué botón ha hecho click.
			switch (v.getId()) {
				case R.id.bAnswer1:
					userAnswer = 1;
					break;
				case R.id.bAnswer2:
					userAnswer = 2;
					break;
				case R.id.bAnswer3:
					userAnswer = 3;
					break;
				case R.id.bAnswer4:
					userAnswer = 4;
					break;
			}
	
			checkRightAnswer();
			
		}


	};
	
	private Button getButtonByAnswerNumber(int answer) {
		// TODO Auto-generated method stub
		switch (answer) { // Devolvemos el botón que relacionado con la respuesta número answer.
			case 1:
				return answerButton[0];
			case 2:
				return answerButton[1];
			case 3:
				return answerButton[2];
			case 4:
				return answerButton[3];
		}
		return null;
	}

	private void checkRightAnswer() {
		// TODO Auto-generated method stub
		if (userAnswer == question.getRightAnswer()) { // Hemos respondido correctamente.
			currentScore = currentScore + timerTask.getCurrentTime(); // Sumo a la puntuación actual el tiempo que me sobra.
			currentScoreText.setText(String.valueOf(currentScore));
		}
		
		if ((userAnswer != question.getRightAnswer()) && (userAnswer != 0)) { // userAnswer==0 quiere decir que no hemos contestado, no ha marcado ninguna opción.
			// Hemos contestado pero erróneamente.
			getButtonByAnswerNumber(userAnswer).setTextColor(getResources().getColor(R.color.red)); // Ha marcado una opción y no es la correcta, la ponemos en rojo.
		}
		// La siguiente instrucción siempre la hará, la de colorear la respuesta correcta.
		// Si no ha marcado ninguna opción, no entrará a los dos if anteriores y coloreará la respuesta correcta. Si entra a alguno de los dos casos
		// anteriores, coloreará la correcta también.
		getButtonByAnswerNumber(question.getRightAnswer()).setTextColor(getResources().getColor(R.color.green)); // Ponemos de verde la respuesta correcta
		
		/*
		if (iterator.hasNext()) { // Hay más preguntas
			if (timerTask.isStopped()) { // El tiempo lo ha parado porque ha contestado. Cuando haces click sobre un botón o contestas para la barra de progreso.
				showMeDialog(NEXT_QUESTION); // Le mostramos un diálogo indicándole si está preparado para la siguiente pregunta.
			}else { // Hay más preguntas pero se le ha acabado el tiempo, mostramos un diálogo de tiempo agotado.
				showMeDialog(TIME_OUT);
			}
		}else { // Si NO hay más preguntas, mostramos el diálogo de final de juego.
			showMeDialog(END_GAME);
		}
		*/
		//SystemClock.sleep(1000);
		//DE ESTA MANERA DEJO MOSTRADA LA RESPUESTA CORRECTA DURANTE 1 SEGUNDO.
		final Handler handler = new Handler(); 
        Timer t = new Timer(); 
        t.schedule(new TimerTask() { //Prepara la ejecución de una tarea después de un tiempo determinado (ms). 
        	//Es una clase que representa una tarea a ejecutar en un tiempo especificado.
        	public void run() { //Dentro de este método definimos las operaciones de la tarea a realizar.
        		handler.post(new Runnable() { //Gracias al método post, podemos acceder desde el hilo secundario al hilo principal.
        			public void run() { //Realizo la tarea que quiero realizar al acabar el tiempo del schedule (1000ms).
        				if (iterator.hasNext()) { // Hay más preguntas
        					if (timerTask.isStopped()) { // El tiempo lo ha parado porque ha contestado. Cuando haces click sobre un botón o contestas para la barra de progreso.
        						showMeDialog(NEXT_QUESTION); // Le mostramos un diálogo indicándole si está preparado para la siguiente pregunta.
        					}else { // Hay más preguntas pero se le ha acabado el tiempo, mostramos un diálogo de tiempo agotado.
        						showMeDialog(TIME_OUT);
        					}
        				}else { // Si NO hay más preguntas, mostramos el diálogo de final de juego.
        					showMeDialog(END_GAME);
        				}
                    } 
                }); 
            } 
        }, 1000); //La tarea empezará en 1 segundo.
        
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (timerTask != null) {
			timerTask.cancel(true);
		}
	}
}
