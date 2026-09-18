const Row = ({ wordLength, inputRefs, handleLetterChange, handleLetterKeyDown, evaluation, show }) => {
  const tiles = Array.from({ length: wordLength }, (_, i) => i).map((i) => {
    return (
      <input
        data-testid={i}
        key={i}
        ref={(element) => (inputRefs.current[i] = element)}
        maxLength="1" 
        type="text" 
        className={`guess-input letter ${evaluation?.[i] || ""}`}
        onChange={(event) => handleLetterChange(event, i)}
        onKeyDown={(event) => handleLetterKeyDown(event, i)}
      />
    )
  })
  return (
    <>
    {show && <section className="guess-row">{tiles}</section>}
    </>
  )
}

export default Row;